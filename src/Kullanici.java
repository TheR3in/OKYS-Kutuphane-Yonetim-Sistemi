public class Kullanici {
    private final int id;
    private String ad;
    private String eposta;
    private String sifre;

    // Parametreli Constructor (Zorunlu ve değişmez kimlik alanı)
    public Kullanici(int id, String ad, String eposta, String sifre) {
        if (id <= 0) {
            throw new IllegalArgumentException("Kullanıcı ID 0'dan büyük olmalıdır.");
        }
        if (ad == null || ad.trim().isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı adı boş olamaz.");
        }
        if (eposta == null || eposta.trim().isEmpty()) {
            throw new IllegalArgumentException("E-posta adresi boş olamaz.");
        }
        if (sifre == null || sifre.trim().isEmpty()) {
            throw new IllegalArgumentException("Şifre boş olamaz.");
        }
        this.id = id;
        this.ad = ad;
        this.eposta = eposta;
        this.sifre = sifre;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    // id 'final' olduğu için setId metodu yazılmamıştır.

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        if (ad == null || ad.trim().isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı adı boş olamaz.");
        }
        this.ad = ad;
    }

    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        if (eposta == null || eposta.trim().isEmpty()) {
            throw new IllegalArgumentException("E-posta adresi boş olamaz.");
        }
        this.eposta = eposta;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        if (sifre == null || sifre.trim().isEmpty()) {
            throw new IllegalArgumentException("Şifre boş olamaz.");
        }
        this.sifre = sifre;
    }
}
