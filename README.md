# Online Kütüphane Yönetim Sistemi (OKYS)

Bu proje, **YBS203 Nesne Yönelimli Programlama** dersi kapsamında geliştirilmiş, konsol (terminal) tabanlı interaktif bir kütüphane otomasyon sistemidir. Sistem, üyelerin kitap arama ve ödünç alma süreçlerini dijitalleştirirken, kütüphane görevlilerinin de bu süreçleri (onaylama, kitap ekleme, süre uzatma vb.) pratik bir şekilde yönetmesine olanak tanır.

## 🗂️ Proje Yapısı

- **`src/`** : Uygulamanın çalışmasını sağlayan, Java ile yazılmış Nesne Yönelimli (OOP) kaynak kodları burada yer alır.
- **`doc/`** : 
  - `okys-SRS.doc` : Sistemin yazılım gereksinim analizi ve işleyiş senaryosu.
  - `okys-DESIGN.drawio` : Sistemin detaylı Sınıf (Class) ve Kullanım Senaryosu (Use Case) UML diyagramlarını içeren tasarım dosyası.
  - `okys-DESIGN.png` : UML diyagramının görsel (resim) hali.
  - `OKYS_Proje_Raporu.md` : Projenin temel rapor dosyası.

<p align="center">
  <img src="doc/okys-DESIGN.png" alt="Sistem UML Tasarımı">
</p>

## 🚀 Temel Özellikler

- **Rol Bazlı Erişim Sistemi:** `Üye` ve `Kütüphane Görevlisi` olmak üzere iki farklı kullanıcı tipi ve bu tiplere özel ayrıştırılmış interaktif menüler.
- **Dinamik Ödünç Alma Döngüsü:** Üyelerin ödünç alma ve süre uzatma isteklerinin önce bir havuza düştüğü, kütüphane yöneticisinin onayından sonra işlemin gerçekleştiği gerçekçi bir iş akışı.
- **Gelişmiş Kitap Yönetimi:** Sisteme kayıtlı kitapları başlığına göre arama, yeni kitap/fiziksel kopya ekleme ve kitapların raftaki kopyalarını barkodlarıyla beraber takip etme.
- **Nesne Yönelimli Mimari (OOP):** Kalıtım (Inheritance), Kapsülleme (Encapsulation) ve Birleştirme (Composition) ilkelerinin (Örn: `Kitap` ve `KitapKopyasi` ilişkisi) katı kurallarla uygulandığı temiz kod mimarisi.

## 🛠️ Nasıl Çalıştırılır?

Projeyi bilgisayarınızda çalıştırmak için **Java (JDK 8 veya üzeri)** kurulu olmalıdır. Terminal veya Komut İstemi'ni (CMD) kullanarak aşağıdaki adımları izleyebilirsiniz:

1. Projenin bulunduğu dizinde terminali açın.
2. `src` klasörünün içine girin:
   ```bash
   cd src
   ```
3. Mevcut tüm Java sınıflarını derleyin:
   ```bash
   javac *.java
   ```
4. Programın ana sınıfını (Main) çalıştırın:
   ```bash
   java Main
   ```

### 🔑 Test İçin Hazır Giriş Bilgileri (Demo Hesaplar)

Sistemi çalıştırdığınızda hemen test edebilmeniz için içerisine varsayılan hesaplar eklenmiştir. Giriş ekranında aşağıdaki bilgileri kullanabilirsiniz:

**👤 Üye Hesabı (Ödünç Alma & Arama İşlemleri):**
- **E-posta:** `ahmet@29mayis.edu.tr`
- **Şifre:** `1234`

**🛡️ Kütüphane Görevlisi Hesabı (Onaylama & Kitap Yönetimi):**
- **E-posta:** `admin@29mayis.edu.tr`
- **Şifre:** `admin`

## 👨‍💻 Geliştirme Notları

Bu proje, herhangi bir harici veritabanı (SQL) kullanılmadan, bilgilerin programın çalıştığı süre boyunca geçici hafızada (`ArrayList`) tutulduğu bir konsol otomasyonu olarak geliştirilmiştir. Projede yer alan kodlar, `doc` klasöründeki UML Sınıf Diyagramına 1'e 1 sadık kalınarak kodlanmıştır.
