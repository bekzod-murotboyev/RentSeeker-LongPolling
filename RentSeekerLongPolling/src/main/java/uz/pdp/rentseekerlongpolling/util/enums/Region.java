package uz.pdp.rentseekerlongpolling.util.enums;

public enum Region {
    TASHKENT(1,"TOSHKENT SHAHRI","ГОРОД ТАШКЕНТ", "TASHKENT CITY"),
    TASHKENT_REGION(2,"TOSHKENT VILOYATI","ТАШКЕНТСКАЯ ОБЛАСТЬ","TASHKENT REGION"),
    ANDIJAN(3,"ANDIJON","АНДИЖАН","ANDIJAN"),
    BUKHARA(4,"BUXORO","БУХАРА","BUKHARA"),
    DZHIZAK(5,"JIZAX","ДЖИЗАК","DZHIZAK"),
    KASHKADARYA(6,"QASHQADARYO","КАШКАДАРЬЯ","KASHKADARYA"),
    NAVOI(7,"NAVOIY","НАВОИ","NAVOI"),
    NAMANGAN(8,"NAMANGAN","НАМАНГАН","NAMANGAN"),
    SAMARKAND(9,"SAMARQAND","САМАРКАНД","SAMARKAND"),
    SURKHANDARYA(10,"SURXONDARYO","СУРХАНДАРЬЯ","SURKHANDARYA"),
    SYRDARYA(11,"SIRDARYO","СЫРДАРЬЯ","SYRDARYA"),
    FERGHANA(12,"FARG'ONA","ФЕРГАНА","FERGHANA"),
    XORAZM(13,"XORAZM","ХОРАЗМ","XORAZM"),
    REPUBLIC_OF_KARAKALPAKSTAN(14,"QORAQALPOG'ISTON","КАРАКАЛПАКСТАН","KARAKALPAKSTAN");


    private int id;
    private String uz;
    private String ru;
    private String eng;

    Region(int id, String uz, String ru, String eng) {
        this.id = id;
        this.uz = uz;
        this.ru = ru;
        this.eng = eng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUz() {
        return uz;
    }

    public void setUz(String uz) {
        this.uz = uz;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }
}
