public class Kullanici {
    private int id;
    private String ad;
    private String eposta;
    private String sifre;

    // Default Constructor
    public Kullanici() {
    }

    // Parametreli Constructor
    public Kullanici(int id, String ad, String eposta, String sifre) {
        this.id = id;
        this.ad = ad;
        this.eposta = eposta;
        this.sifre = sifre;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }
}
