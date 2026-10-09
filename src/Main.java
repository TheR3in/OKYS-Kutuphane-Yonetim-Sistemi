import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Geçici Hafıza (Veritabanı yerine geçecek Listeler)
    private static List<Kullanici> kullanicilar = new ArrayList<>();
    private static List<Kitap> kitaplar = new ArrayList<>();
    private static List<OduncKaydi> oduncKayitlari = new ArrayList<>();
    private static List<OduncKaydi> beklemedekiTalepler = new ArrayList<>(); // Görevli onayı bekleyen sıfır ödünç talepleri
    private static List<OduncKaydi> uzatmaTalepleri = new ArrayList<>(); // Görevli onayı bekleyen süre uzatma talepleri
    
    // Üyenin aynı kitaba birden fazla talep açmasını engellemek için (UyeID-ISBN formatında tutar)
    private static List<String> uyeKitapIstekGecmisi = new ArrayList<>();
    
    private static Scanner scanner = new Scanner(System.in);

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

        // 2. Hazır Kitaplar ve Kopyaları
        Kitap kitap1 = new Kitap("978-0134685991", "Python ile Nesne Yönelimli Programlama", "Alan Kay", 2026, "Ödünç Verilebilir");
        kitap1.kopyaEkle(new KitapKopyasi("B-1001"));
        kitap1.kopyaEkle(new KitapKopyasi("B-1002"));

        Kitap kitap2 = new Kitap("978-0201633610", "Tasarım Desenleri", "Erich Gamma", 1994, "Ödünç Verilebilir");
        kitap2.kopyaEkle(new KitapKopyasi("B-2001"));

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
                if (k.getEposta().equals(eposta) && k.getSifre().equals(sifre)) {
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
        if (uye.getOkudugumKitaplar().isEmpty()) {
            System.out.println("Şu an üzerinizde onaylı ve kayıtlı kitap bulunmamaktadır.");
            return;
        }
        for (OduncKaydi kayit : uye.getOkudugumKitaplar()) {
            System.out.println("İşlem No: " + kayit.getIslemNo() + " | Barkod: " + kayit.getKopya().getBarkodNo() 
                + " | Ödünç Tarihi: " + kayit.getOduncTarihi() + " | Son Teslim: " + kayit.getSonTeslimTarihi());
        }
    }

    private static void kitapAraVeListele() {
        System.out.print("Aramak istediğiniz kitabın başlığı (Tümünü listelemek için 'enter'a basın): ");
        String aranan = scanner.nextLine().toLowerCase();
        System.out.println("\n--- SONUÇLAR ---");
        for (Kitap k : kitaplar) {
            if (k.getBaslik().toLowerCase().contains(aranan)) {
                System.out.println(k.kitapBilgisi() + " | Uygun Kopya Sayısı: " + k.getFizikselKopyalar().size());
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
        
        if (secilenKitap == null || !secilenKitap.oduncIste(null)) {
            System.out.println("HATA: Kitap bulunamadı veya stokta uygun kopya yok!");
            return;
        }

        // AYNI KİTABA BİRDEN FAZLA İSTEK ATMASINI ENGELLEYEN BLOK
        String istekKey = uye.getId() + "-" + secilenKitap.getIsbn();
        if (uyeKitapIstekGecmisi.contains(istekKey)) {
            System.out.println("HATA: Bu kitap için zaten bir ödünç alma talebi gönderdiniz veya kitap halihazırda sizde bulunuyor!");
            return;
        }

        KitapKopyasi kopya = secilenKitap.getFizikselKopyalar().remove(0);
        
        OduncKaydi talep = new OduncKaydi(uye, kopya, "Onay Bekliyor", "Onay Bekliyor");
        beklemedekiTalepler.add(talep);
        
        // Talebin spam olmasını engellemek için hafızaya kaydet
        uyeKitapIstekGecmisi.add(istekKey);
        
        System.out.println("✅ Talebiniz Kütüphane Görevlisi onayına gönderildi! Barkod: " + kopya.getBarkodNo());
    }

    private static void sureUzat(Uye uye) {
        System.out.println("\n--- SÜRE UZATMA EKRANI ---");
        if (uye.getOkudugumKitaplar().isEmpty()) {
            System.out.println("Şu an üzerinizde onaylı ve kayıtlı kitap bulunmamaktadır.");
            return;
        }
        
        // Önce kitapları listele
        for (OduncKaydi kayit : uye.getOkudugumKitaplar()) {
            System.out.println("İşlem No: " + kayit.getIslemNo() + " | Barkod: " + kayit.getKopya().getBarkodNo() + " | Son Teslim: " + kayit.getSonTeslimTarihi());
        }
        
        System.out.print("Süresini uzatmak istediğiniz İşlem No: ");
        try {
            int islemNo = Integer.parseInt(scanner.nextLine());
            for (OduncKaydi kayit : uye.getOkudugumKitaplar()) {
                if (kayit.getIslemNo() == islemNo) {
                    if (uzatmaTalepleri.contains(kayit)) {
                        System.out.println("HATA: Bu kayıt için zaten onay bekleyen bir uzatma talebiniz var!");
                        return;
                    }
                    uzatmaTalepleri.add(kayit);
                    System.out.println("✅ Süre uzatma talebiniz Görevliye iletildi! Onaylandığında güncellenecektir.");
                    return;
                }
            }
            System.out.println("HATA: Bu numaraya ait işlem bulunamadı.");
        } catch (Exception e) {
            System.out.println("HATA: Sadece sayı girmelisiniz!");
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
            System.out.println("2. Yeni Kitap Ekle");
            System.out.println("3. Kitap Bilgilerini Güncelle");
            System.out.println("4. Bekleyen Ödünç Taleplerini Onayla (" + beklemedekiTalepler.size() + " Bekleyen)");
            System.out.println("5. Bekleyen Süre Uzatma Taleplerini Onayla (" + uzatmaTalepleri.size() + " Bekleyen)");
            System.out.println("6. Çıkış Yap (Ana Menüye Dön)");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            switch (secim) {
                case "1": kitapAraVeListele(); break;
                case "2": yeniKitapEkle(); break;
                case "3": kitapGuncelle(); break;
                case "4": talepleriOnayla(); break;
                case "5": uzatmaTalepleriniOnayla(); break;
                case "6": cikis = true; System.out.println("Ana menüye dönülüyor..."); break;
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

        System.out.print("Eklenecek Kopya Barkodu: ");
        String barkod = scanner.nextLine();

        Kitap yeniKitap = new Kitap(isbn, baslik, yazar, yil, "Ödünç Verilebilir");
        yeniKitap.kopyaEkle(new KitapKopyasi(barkod));
        kitaplar.add(yeniKitap);

        System.out.println("\n✅ BAŞARILI! Kitap kütüphane sistemine eklendi.");
    }

    private static void kitapGuncelle() {
        System.out.print("Güncellenecek kitabın ISBN numarası: ");
        String isbn = scanner.nextLine();
        for (Kitap k : kitaplar) {
            if (k.getIsbn().equals(isbn)) {
                System.out.println("Mevcut Durum: " + k.getDurum());
                System.out.print("Yeni Durum (Örn: Ödünç Verilemez / Rafta): ");
                String yeniDurum = scanner.nextLine();
                k.setDurum(yeniDurum);
                System.out.println("✅ Kitap durumu başarıyla güncellendi!");
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
        List<OduncKaydi> islenenTalepler = new ArrayList<>();
        
        for (OduncKaydi talep : beklemedekiTalepler) {
            System.out.println("\nTalep İşlem No: " + talep.getIslemNo() + " | Üye: " + talep.getUye().getAd() + " | Kopya: " + talep.getKopya().getBarkodNo());
            System.out.print("Bu talebi onaylıyor musunuz? (E/H): ");
            String cevap = scanner.nextLine().toUpperCase();
            
            if (cevap.equals("E")) {
                talep.setOduncTarihi("Bugün");
                talep.setSonTeslimTarihi("14 Gün Sonra");
                
                oduncKayitlari.add(talep);
                talep.getUye().getOkudugumKitaplar().add(talep);
                talep.getKopya().getOduncListesi().add(talep);
                
                System.out.println("✅ Talep ONAYLANDI ve sistem kayıtlarına işlendi!");
            } else {
                System.out.println("❌ Talep Bekletildi / Reddedildi.");
                // Reddedilirse tekrar isteyebilmesi için basitçe reddedildi sayıyoruz
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
        List<OduncKaydi> islenenUzatma = new ArrayList<>();
        
        for (OduncKaydi talep : uzatmaTalepleri) {
            System.out.println("\nUzatma Talep İşlem No: " + talep.getIslemNo() + " | Üye: " + talep.getUye().getAd() + " | Mevcut Son Teslim: " + talep.getSonTeslimTarihi());
            System.out.print("Süreyi (+14 Gün) uzatmayı onaylıyor musunuz? (E/H): ");
            String cevap = scanner.nextLine().toUpperCase();
            
            if (cevap.equals("E")) {
                talep.setSonTeslimTarihi(talep.getSonTeslimTarihi() + " (+14 Gün Ek Onaylı)");
                System.out.println("✅ Uzatma Talebi ONAYLANDI!");
            } else {
                System.out.println("❌ Uzatma Talebi REDDEDİLDİ. Süre sabit kaldı.");
            }
            islenenUzatma.add(talep);
        }
        uzatmaTalepleri.removeAll(islenenUzatma);
    }
}
