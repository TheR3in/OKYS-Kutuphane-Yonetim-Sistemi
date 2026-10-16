public class OduncAlma {
    private static int islemSayaci = 0;
    private final int islemNo;
    
    private final Uye uye;
    private final KitapKopyasi kopya;
    private final String oduncTarihi;
    private String sonTeslimTarihi;
    private String teslimTarihi;

    // Parametreli Constructor
    public OduncAlma(Uye uye, KitapKopyasi kopya, String oduncTarihi, String sonTeslimTarihi) {
        if (uye == null) {
            throw new IllegalArgumentException("Üye bilgisi boş olamaz.");
        }
        if (kopya == null) {
            throw new IllegalArgumentException("Kitap kopyası boş olamaz.");
        }
        if (oduncTarihi == null || oduncTarihi.trim().isEmpty()) {
            throw new IllegalArgumentException("Ödünç tarihi boş olamaz.");
        }
        islemSayaci++;
        this.islemNo = islemSayaci;
        this.uye = uye;
        this.kopya = kopya;
        this.oduncTarihi = oduncTarihi;
        this.sonTeslimTarihi = sonTeslimTarihi;
        this.teslimTarihi = null; // Başlangıçta henüz teslim edilmedi

        // Çift yönlü ilişkiyi kaydet
        kopya.oduncEkle(this);
        uye.oduncEkle(this);
    }

    // Getters
    public static int getIslemSayaci() {
        return islemSayaci;
    }

    public int getIslemNo() {
        return islemNo;
    }

    public Uye getUye() {
        return uye;
    }

    public KitapKopyasi getKopya() {
        return kopya;
    }

    public String getOduncTarihi() {
        return oduncTarihi;
    }

    public String getSonTeslimTarihi() {
        return sonTeslimTarihi;
    }

    public void setSonTeslimTarihi(String sonTeslimTarihi) {
        if (sonTeslimTarihi == null || sonTeslimTarihi.trim().isEmpty()) {
            throw new IllegalArgumentException("Son teslim tarihi boş olamaz.");
        }
        this.sonTeslimTarihi = sonTeslimTarihi;
    }

    public String getTeslimTarihi() {
        return teslimTarihi;
    }

    // Davranış metodu: İade işlemi
    public void teslimEt(String teslimTarihi) {
        if (teslimTarihi == null || teslimTarihi.trim().isEmpty()) {
            throw new IllegalArgumentException("Teslim tarihi boş olamaz.");
        }
        this.teslimTarihi = teslimTarihi;
        this.kopya.setDurum(KitapDurumu.MEVCUT);
    }
}
