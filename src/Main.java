import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Geçici Hafıza (Veritabanı yerine geçecek Listeler)
    private static List<Kullanici> kullanicilar = new ArrayList<>();
    private static List<Kitap> kitaplar = new ArrayList<>();
    private static List<OduncAlma> oduncIslemleri = new ArrayList<>();
    
    // Talep Yönetimi Havuzları
    private static List<OduncTalep> beklemedekiTalepler = new ArrayList<>(); // Görevli onayı bekleyen ödünç talepleri
    private static List<OduncAlma> uzatmaTalepleri = new ArrayList<>(); // Görevli onayı bekleyen süre uzatma talepleri
    
    // Üyenin aynı kitaba mükerrer talep açmasını engellemek için (UyeID-ISBN formatında tutar)
    private static List<String> uyeKitapIstekGecmisi = new ArrayList<>();
    
    private static Scanner scanner = new Scanner(System.in);

    // Basit ara talep sınıfı (Görevli onaylayana kadar OduncAlma nesnesine dönüşecek taslak)
    private static class OduncTalep {
        private static int talepSayaci = 0;
        private final int talepNo;
        private final Uye uye;
        private final KitapKopyasi kopya;

        public OduncTalep(Uye uye, KitapKopyasi kopya) {
            talepSayaci++;
            this.talepNo = talepSayaci;
            this.uye = uye;
            this.kopya = kopya;
        }

        public int getTalepNo() { return talepNo; }
        public Uye getUye() { return uye; }
        public KitapKopyasi getKopya() { return kopya; }
    }

    public static void main(String[] args) {
        baslangicVerileriniYukle();
        sistemiBaslat();
    }

    private static void baslangicVerileriniYukle() {
        // 1. Hazır Kullanıcılar
        Uye uye1 = new Uye(1, "Ahmet Yasin Akkuş", "ahmet@29mayis.edu.tr", "1234");
        KutuphaneGorevlisi admin = new KutuphaneGorevlisi(2, "Admin Görevli", "admin@29mayis.edu.tr", "admin");
        kullanicilar.add(uye1);
        kullanicilar.add(admin);

        // 2. Hazır Kitaplar ve Kopyaları (Slayt 5'teki iki yönlü otomatik bağlama ile)
        Kitap kitap1 = new Kitap("978-0134685991", "Python ile Nesne Yönelimli Programlama", "Alan Kay", 2026);
        new KitapKopyasi("B-1001", kitap1); // Constructor içinde otomatik olarak kitap1'e eklenir
        new KitapKopyasi("B-1002", kitap1);

        Kitap kitap2 = new Kitap("978-0201633610", "Tasarım Desenleri", "Erich Gamma", 1994);
        new KitapKopyasi("B-2001", kitap2);

        kitaplar.add(kitap1);
        kitaplar.add(kitap2);
    }

    private static void sistemiBaslat() {
        while (true) {
            System.out.println("\n=============================================");
            System.out.println("  OKYS - ONLINE KÜTÜPHANE YÖNETİM SİSTEMİ  ");
            System.out.println("=============================================");
            System.out.print("E-posta adresiniz: ");
            String eposta = scanner.nextLine();
            System.out.print("Şifreniz: ");
            String sifre = scanner.nextLine();

            Kullanici girisYapan = null;
            for (Kullanici k : kullanicilar) {
                if (k.getEposta().equalsIgnoreCase(eposta) && k.getSifre().equals(sifre)) {
                    girisYapan = k;
                    break;
                }
            }

            if (girisYapan == null) {
                System.out.println("HATA: Hatalı e-posta veya şifre girdiniz. Lütfen tekrar deneyin!");
            } else {
                System.out.println("\nHoş geldiniz, " + girisYapan.getAd() + "!");
                if (girisYapan instanceof KutuphaneGorevlisi) {
                    gorevliMenusu((KutuphaneGorevlisi) girisYapan);
                } else if (girisYapan instanceof Uye) {
                    uyeMenusu((Uye) girisYapan);
                }
            }
        }
    }

    // ----------------------------------------------------
    // ÜYE MENÜSÜ VE METOTLARI
    // ----------------------------------------------------
    private static void uyeMenusu(Uye uye) {
        boolean cikis = false;
        while (!cikis) {
            System.out.println("\n--- ÜYE MENÜSÜ ---");
            System.out.println("1. Kitap Ara / Listele");
            System.out.println("2. Ödünç Aldığım Kitapları Görüntüle");
            System.out.println("3. Kitap Ödünç Talebi Oluştur");
            System.out.println("4. Ödünç Süresini Uzat (Onaylı İşlem)");
            System.out.println("5. Çıkış Yap (Ana Menüye Dön)");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1": kitapAraVeListele(); break;
                case "2": oduncAldigimKitaplar(uye); break;
                case "3": kitapOduncTalebiOlustur(uye); break;
                case "4": sureUzat(uye); break;
                case "5": cikis = true; System.out.println("Ana menüye dönülüyor..."); break;
                default: System.out.println("HATA: Geçersiz seçim!");
            }
        }
    }

    private static void oduncAldigimKitaplar(Uye uye) {
        System.out.println("\n--- ÖDÜNÇ ALDIĞIM / OKUDUĞUM KİTAPLAR ---");
        if (uye.getOkuduguKitaplar().isEmpty()) {
            System.out.println("Şu an üzerinizde onaylı ve kayıtlı kitap bulunmamaktadır.");
            return;
        }
        for (OduncAlma kayit : uye.getOkuduguKitaplar()) {
            String teslimDurumu = (kayit.getTeslimTarihi() == null) ? "Devam Ediyor" : "Teslim Edildi: " + kayit.getTeslimTarihi();
            System.out.println("İşlem No: " + kayit.getIslemNo() +
                               " | Kitap: " + kayit.getKopya().getKitap().getBaslik() +
                               " | Barkod: " + kayit.getKopya().getBarkod() + 
                               " | Ödünç: " + kayit.getOduncTarihi() + 
                               " | Son Teslim: " + kayit.getSonTeslimTarihi() +
                               " | Durum: " + teslimDurumu);
        }
    }

    private static void kitapAraVeListele() {
        System.out.print("Aramak istediğiniz kitabın başlığı (Tümünü listelemek için Enter'a basın): ");
        String aranan = scanner.nextLine().toLowerCase();
        System.out.println("\n--- ARAMA SONUÇLARI ---");
        for (Kitap k : kitaplar) {
            if (k.getBaslik().toLowerCase().contains(aranan) || k.getYazar().toLowerCase().contains(aranan)) {
                int mevcutKopya = 0;
                for (KitapKopyasi kopya : k.getFizikselKopyalar()) {
                    if (kopya.getDurum() == KitapDurumu.MEVCUT) {
                        mevcutKopya++;
                    }
                }
                System.out.println(k.kitapBilgisi() + " | Rafta Mevcut Kopya: " + mevcutKopya);
                for (KitapKopyasi kopya : k.getFizikselKopyalar()) {
                    System.out.println("   └─ Kopya Barkod: " + kopya.getBarkod() + " [" + kopya.getDurum() + "]");
                }
            }
        }
    }

    private static void kitapOduncTalebiOlustur(Uye uye) {
        System.out.print("Talep etmek istediğiniz kitabın ISBN numarasını girin: ");
        String isbn = scanner.nextLine();
        
        Kitap secilenKitap = null;
        for (Kitap k : kitaplar) {
            if (k.getIsbn().equals(isbn)) {
                secilenKitap = k; 
                break;
            }
        }
        
        if (secilenKitap == null) {
            System.out.println("HATA: Bu ISBN numarasına ait kitap bulunamadı!");
            return;
        }

        // Mükerrer talep kontrolü
        String istekKey = uye.getId() + "-" + secilenKitap.getIsbn();
        if (uyeKitapIstekGecmisi.contains(istekKey)) {
            System.out.println("HATA: Bu kitap için zaten bir ödünç alma talebi gönderdiniz veya kitap halihazırda sizde bulunuyor!");
            return;
        }

        // Rafta MEVCUT bir kopya bul
        KitapKopyasi uygunKopya = null;
        for (KitapKopyasi kopya : secilenKitap.getFizikselKopyalar()) {
            if (kopya.getDurum() == KitapDurumu.MEVCUT) {
                // Halihazırda onay bekleyen bir talepte rezerve mi?
                boolean talepteVar = false;
                for (OduncTalep talep : beklemedekiTalepler) {
                    if (talep.getKopya() == kopya) {
                        talepteVar = true;
                        break;
                    }
                }
                if (!talepteVar) {
                    uygunKopya = kopya;
                    break;
                }
            }
        }

        if (uygunKopya == null) {
            System.out.println("HATA: Bu kitabın şu anda rafta uygun ve boşta bir kopyası bulunmamaktadır!");
            return;
        }

        OduncTalep yeniTalep = new OduncTalep(uye, uygunKopya);
        beklemedekiTalepler.add(yeniTalep);
        uyeKitapIstekGecmisi.add(istekKey);
        
        System.out.println("✅ Talebiniz Kütüphane Görevlisi onayına iletildi! Atanan Barkod: " + uygunKopya.getBarkod());
    }

    private static void sureUzat(Uye uye) {
        System.out.println("\n--- SÜRE UZATMA EKRANI ---");
        List<OduncAlma> aktifOduncler = new ArrayList<>();
        for (OduncAlma kayit : uye.getOkuduguKitaplar()) {
            if (kayit.getTeslimTarihi() == null) {
                aktifOduncler.add(kayit);
            }
        }

        if (aktifOduncler.isEmpty()) {
            System.out.println("Şu an üzerinizde aktif (iade edilmemiş) bir kitap bulunmamaktadır.");
            return;
        }
        
        for (OduncAlma kayit : aktifOduncler) {
            System.out.println("İşlem No: " + kayit.getIslemNo() +
                               " | Barkod: " + kayit.getKopya().getBarkod() + 
                               " | Kitap: " + kayit.getKopya().getKitap().getBaslik() +
                               " | Son Teslim: " + kayit.getSonTeslimTarihi());
        }
        
        System.out.print("Süresini uzatmak istediğiniz İşlem No: ");
        try {
            int islemNo = Integer.parseInt(scanner.nextLine());
            for (OduncAlma kayit : aktifOduncler) {
                if (kayit.getIslemNo() == islemNo) {
                    if (uzatmaTalepleri.contains(kayit)) {
                        System.out.println("HATA: Bu kayıt için zaten onay bekleyen bir uzatma talebiniz var!");
                        return;
                    }
                    uzatmaTalepleri.add(kayit);
                    System.out.println("✅ Süre uzatma talebiniz Görevliye iletildi! Onaylandığında süreniz güncellenecektir.");
                    return;
                }
            }
            System.out.println("HATA: Bu numaraya ait aktif işlem bulunamadı.");
        } catch (NumberFormatException e) {
            System.out.println("HATA: Lütfen geçerli bir sayı giriniz!");
        }
    }

    // ----------------------------------------------------
    // GÖREVLİ MENÜSÜ VE METOTLARI
    // ----------------------------------------------------
    private static void gorevliMenusu(KutuphaneGorevlisi gorevli) {
        boolean cikis = false;
        while (!cikis) {
            System.out.println("\n--- GÖREVLİ MENÜSÜ ---");
            System.out.println("1. Kitapları Listele / Ara");
            System.out.println("2. Yeni Kitap & Kopya Ekle");
            System.out.println("3. Mevcut Kitaba Yeni Fiziksel Kopya Ekle");
            System.out.println("4. Kitap Bilgilerini Güncelle (Başlık / Yazar / Yıl)");
            System.out.println("5. Bekleyen Ödünç Taleplerini Onayla (" + beklemedekiTalepler.size() + " Bekleyen)");
            System.out.println("6. Bekleyen Süre Uzatma Taleplerini Onayla (" + uzatmaTalepleri.size() + " Bekleyen)");
            System.out.println("7. Kitap İadesi Al");
            System.out.println("8. Çıkış Yap (Ana Menüye Dön)");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1": kitapAraVeListele(); break;
                case "2": yeniKitapEkle(); break;
                case "3": kitabaKopyaEkle(); break;
                case "4": kitapGuncelle(); break;
                case "5": talepleriOnayla(); break;
                case "6": uzatmaTalepleriniOnayla(); break;
                case "7": kitapIadeAl(); break;
                case "8": cikis = true; System.out.println("Ana menüye dönülüyor..."); break;
                default: System.out.println("HATA: Geçersiz seçim!");
            }
        }
    }

    private static void yeniKitapEkle() {
        System.out.print("Kitabın ISBN numarası: ");
        String isbn = scanner.nextLine();
        System.out.print("Kitabın Başlığı: ");
        String baslik = scanner.nextLine();
        System.out.print("Kitabın Yazarı: ");
        String yazar = scanner.nextLine();
        System.out.print("Yayın Yılı: ");
        
        int yil = 0;
        try {
            yil = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("HATA: Yıl rakamlardan oluşmalıdır. İşlem iptal edildi.");
            return;
        }

        System.out.print("İlk Kopya İçin Barkod: ");
        String barkod = scanner.nextLine();

        try {
            Kitap yeniKitap = new Kitap(isbn, baslik, yazar, yil);
            new KitapKopyasi(barkod, yeniKitap); // Slayt 5: Constructor otomatik olarak iki yönlü bağlar
            kitaplar.add(yeniKitap);
            System.out.println("\n✅ BAŞARILI! Yeni kitap ve 1 adet fiziksel kopyası sisteme eklendi.");
        } catch (IllegalArgumentException e) {
            System.out.println("HATA: " + e.getMessage());
        }
    }

    private static void kitabaKopyaEkle() {
        System.out.print("Kopya eklemek istediğiniz kitabın ISBN numarası: ");
        String isbn = scanner.nextLine();
        Kitap hedefKitap = null;
        for (Kitap k : kitaplar) {
            if (k.getIsbn().equals(isbn)) {
                hedefKitap = k;
                break;
            }
        }
        if (hedefKitap == null) {
            System.out.println("HATA: Kitap bulunamadı!");
            return;
        }

        System.out.print("Yeni Kopyanın Barkodu: ");
        String barkod = scanner.nextLine();
        try {
            new KitapKopyasi(barkod, hedefKitap);
            System.out.println("✅ Yeni fiziksel kopya (" + barkod + ") başarıyla " + hedefKitap.getBaslik() + " kitabına eklendi.");
        } catch (IllegalArgumentException e) {
            System.out.println("HATA: " + e.getMessage());
        }
    }

    private static void kitapGuncelle() {
        System.out.print("Güncellenecek kitabın ISBN numarası: ");
        String isbn = scanner.nextLine();
        for (Kitap k : kitaplar) {
            if (k.getIsbn().equals(isbn)) {
                System.out.println("Seçilen Kitap: " + k.getBaslik() + " (" + k.getYazar() + ")");
                System.out.print("Yeni Başlık (Boş bırakılırsa değişmez): ");
                String yeniBaslik = scanner.nextLine();
                if (!yeniBaslik.trim().isEmpty()) {
                    k.setBaslik(yeniBaslik);
                }

                System.out.print("Yeni Yazar (Boş bırakılırsa değişmez): ");
                String yeniYazar = scanner.nextLine();
                if (!yeniYazar.trim().isEmpty()) {
                    k.setYazar(yeniYazar);
                }

                System.out.print("Yeni Yayın Yılı (Boş bırakılırsa değişmez): ");
                String yilStr = scanner.nextLine();
                if (!yilStr.trim().isEmpty()) {
                    try {
                        k.setYayinYili(Integer.parseInt(yilStr));
                    } catch (NumberFormatException e) {
                        System.out.println("UYARI: Geçersiz yıl formatı, yıl değiştirilmedi.");
                    }
                }

                System.out.println("✅ Kitap bilgileri başarıyla güncellendi!");
                return;
            }
        }
        System.out.println("HATA: Kitap bulunamadı.");
    }

    private static void talepleriOnayla() {
        if (beklemedekiTalepler.isEmpty()) {
            System.out.println("Onay bekleyen ödünç alma talebi bulunmamaktadır.");
            return;
        }
        List<OduncTalep> islenenTalepler = new ArrayList<>();
        
        for (OduncTalep talep : beklemedekiTalepler) {
            System.out.println("\nTalep No: " + talep.getTalepNo() +
                               " | Üye: " + talep.getUye().getAd() + 
                               " | Kitap: " + talep.getKopya().getKitap().getBaslik() +
                               " | Kopya Barkod: " + talep.getKopya().getBarkod());
            System.out.print("Bu talebi onaylıyor musunuz? (E/H): ");
            String cevap = scanner.nextLine().toUpperCase();
            
            if (cevap.equals("E")) {
                // Slayt 3 & 4 kuralı: Kopyanın durumu ODUNCTE olarak güncellenir
                talep.getKopya().setDurum(KitapDurumu.ODUNCTE);
                
                // OduncAlma nesnesi oluşturulur (Constructor içinde otomatik olarak üye ve kopyaya bağlanır)
                OduncAlma kayit = new OduncAlma(talep.getUye(), talep.getKopya(), "Bugün (16.10.2026)", "14 Gün Sonra (30.10.2026)");
                oduncIslemleri.add(kayit);
                
                System.out.println("✅ Talep ONAYLANDI! İşlem No: " + kayit.getIslemNo() + " olarak sisteme işlendi.");
            } else {
                System.out.println("❌ Talep Reddedildi.");
                uyeKitapIstekGecmisi.remove(talep.getUye().getId() + "-" + talep.getKopya().getKitap().getIsbn());
            }
            islenenTalepler.add(talep); 
        }
        beklemedekiTalepler.removeAll(islenenTalepler);
    }

    private static void uzatmaTalepleriniOnayla() {
        if (uzatmaTalepleri.isEmpty()) {
            System.out.println("Onay bekleyen süre uzatma talebi bulunmamaktadır.");
            return;
        }
        List<OduncAlma> islenenUzatma = new ArrayList<>();
        
        for (OduncAlma kayit : uzatmaTalepleri) {
            System.out.println("\nİşlem No: " + kayit.getIslemNo() + 
                               " | Üye: " + kayit.getUye().getAd() + 
                               " | Barkod: " + kayit.getKopya().getBarkod() + 
                               " | Mevcut Son Teslim: " + kayit.getSonTeslimTarihi());
            System.out.print("Süreyi (+14 Gün) uzatmayı onaylıyor musunuz? (E/H): ");
            String cevap = scanner.nextLine().toUpperCase();
            
            if (cevap.equals("E")) {
                kayit.setSonTeslimTarihi(kayit.getSonTeslimTarihi() + " (+14 Gün Uzatıldı)");
                System.out.println("✅ Uzatma Talebi ONAYLANDI!");
            } else {
                System.out.println("❌ Uzatma Talebi REDDEDİLDİ. Süre sabit kaldı.");
            }
            islenenUzatma.add(kayit);
        }
        uzatmaTalepleri.removeAll(islenenUzatma);
    }

    private static void kitapIadeAl() {
        System.out.print("İade edilecek kopyanın Barkod numarasını girin: ");
        String barkod = scanner.nextLine();

        OduncAlma aktifKayit = null;
        for (OduncAlma kayit : oduncIslemleri) {
            if (kayit.getKopya().getBarkod().equalsIgnoreCase(barkod) && kayit.getTeslimTarihi() == null) {
                aktifKayit = kayit;
                break;
            }
        }

        if (aktifKayit == null) {
            System.out.println("HATA: Bu barkoda ait aktif (teslim edilmemiş) bir ödünç alma işlemi bulunamadı!");
            return;
        }

        // Davranış metoduyla teslim al ve kopyanın durumunu MEVCUT yap
        aktifKayit.teslimEt("Bugün (16.10.2026)");
        uyeKitapIstekGecmisi.remove(aktifKayit.getUye().getId() + "-" + aktifKayit.getKopya().getKitap().getIsbn());
        System.out.println("✅ Kitap kopyası başarıyla iade alındı ve raftaki durumu MEVCUT olarak güncellendi!");
    }
}
