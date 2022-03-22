package uz.pdp.rentseekerlongpolling.util.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum District {
    //TASHKENT
    OLMAZOR(1,"OLMAZOR","OЛМАЗОР","ALMAZAR",1),
    BEKTEMIR(2,"BEKTEMIR","БЕКТЕМИР","BEKTEMIR",1),
    MIROBOD(3,"MIROBOD","МИРАБАД","MIROBOD",1),
    MIRZO_ULUGBEK(4,"MIRZO_ULUGBEK","МИРЗА_УЛУГБЕК","MIRZO_ULUGBEK",1),
    SERGELI(5,"SERGELI","СЕРГЕЛИ","SERGELI",1),
    UCHTEPA(6,"UCHTEPA","УЧТЕПА","UCHTEPA",1),
    SHAYXONTOHUR(7,"SHAYXONTOHUR","ШАЙХАНТАХУР","SHAYXONTOHUR",1),
    YASHNOBOD(8,"YASHNOBOD","ЯШНАБАД","YASHNOBOD",1),
    CHILONZOR(9,"CHILONZOR","ЧИЛАНЗАР","CHILONZOR",1),
    YUNUSOBOD(10,"YUNUSOBOD","ЮНУСАБАД","YUNUSOBOD",1),
    YAKKASAROY(11,"YAKKASAROY","ЯККАСАРАЙ","YAKKASAROY",1),
    //TASHKENT_DISTRICT
    BEKOBOD(12,"BEKOBOD","БЕКАБАД","BEKOBOD",2),
    BOSTONLIQ(13,"BO`STONLIQ","БОСТАН","BOSTONLIQ",2),
    BOKA(14,"BO`KA","БУКИН","BOKA",2),
    CHINOZ(15,"CHINOZ","ЧИНАЗ","CHINOZ",2),
    QIBRAY(16,"QIBRAY","КИБРАЙ","QIBRAY",2),
    OHANGARON(17,"OHANGARON","АХАНГАРАН","OHANGARON",2),
    OQQORGON(18,"OQQO`RG`ON","АККУРГАН","OQORKHON",2),
    PARKENT(19,"PARKENT","ПАРКЕНТ","PARKENT",2),
    PISKENT(20,"PISKENT","ПСКЕНТ","PISKENT",2),
    QUYI_CHIRCHIQ(21,"QUYI-CHIRCHIQ","КУЙИЧИРЧИК","QUYI-CHIRCHIQ",2),
    ZANGIOTA(22,"ZANGIOTA","ЗАНГИАТ","ZANGIOTA",2),
    ORTA_CHIRCHIQ(23,"O`RTA-CHIRCHIQ","УРТАЧИРЧИК","ORTA-CHIRCHIQ",2),
    YANGIYOL(24,"YANGIYO`L","ЯНГИЮЛЬ","YANGIYOL",2),
    YUQORI_CHIRCHIQ(25,"YUQORI-CHIRCHIQ","ЮКОРИЧИРЧИК","YUQORI-CHIRCHIQ",2),
    //ANDIJAN
    SHAHRIXON(26,"SHAHRIXON","ШАХРИХАН","SHAHRIXON",3),
    ASAKA(27,"ASAKA","АСАКА","ASAKA",3),
    QORGONTEPA(28,"QO`RG`ONTEPA","КАРХАНТЕПА","QARKHANTEPA",3),
    ANDIJON(29,"ANDIJON","АНДИЖАН","ANDIJAN",3),
    BALIQCHI(30,"BALIQCHI","БАЛИКЧИ","BALIQCHI",3),
    OLTINKOL(31,"OLTINKO`L","АЛТИНКОЛ","OLTINKOL",3),
    JALAQUDUQ(32,"JALAQUDUQ","ДЖАЛАКУДУК","JALAQUDUQ",3),
    IZBOSKAN(33,"IZBOSKAN","ИЗБАСКАН","IZBOSKAN",3),
    PAXTAOBOD(34,"PAXTAOBOD","ПАХТААБАД","PAXTAOBOD",3),
    MARHAMAT(35,"MARHAMAT","МАРАХАМАТ","MARHAMAT",3),
    XOJAOBOD(36,"XO`JAOBOD","ХАДЖААБАД","XOJAOBOD",3),
    BULOQBOSHI(37,"BULOQBOSHI","БУЛАКБАШИН","BULOQBOSHI",3),
    BOSTON(38,"BO`STON","БОЗСКИ","BOSTON",3),
    ULUGNOR(39,"ULUG`NOR","УЛУГНОР","ULUGNOR",3),
    XONOBOD(40,"XONOBOD","ХОДЖААБАД","XONOBOD",3),
    //BUKHARA
    BUXORO(41,"BUXORO","БУХАРА","BUKHARA",4),
    GIJDUVON(42,"G`IJDUVON","ГИЖДУВАН","GIJDUVAN",4),
    JONDOR(43,"JONDOR","ЖОНДОР","JONDOR",4),
    KOGON(44,"KOGON","КАГАН","KAGAN",4),
    OLOT(45,"OLOT","АЛАТ","OLOT",4),
    PESHKU(46,"PESHKU","ПЕШКУН","PESHKU",4),
    QORAKOL(47,"QORAKO`L","КАРАКУЛЬ","QORAKOL",4),
    QOROVULBOZOR(48,"QOROVULBOZOR","КАРАУЛБАЗАР","QOROVULBOZOR",4),
    ROMITAN(49,"ROMITAN","РОМИТАН","ROMITAN",4),
    SHOFIRKON(50,"SHOFIRKON","ШАФИРКАН","SHOFIRKON",4),
    VOBKENT(51,"VOBKENT","ВАБКЕНТ","VOBKENT",4),
    //DZHIZAK
    ARNASOY(52,"ARNASOY","АРНАСАЙ","ARNASOY",5),
    BAXMAL(53,"BAXMAL","БАХМАЛЬ","BAXMAL",5),
    DOSTLIK(54,"DO`STLIK","ДУСТЛИК","DOSTLIK",5),
    FORISH(55,"FORISH","ФАРИШ","FORISH",5),
    GALLAOROL(56,"G`ALLAOROL","ГАЛЛЯАРАЛЬ","GALLAOROL",5),
    JIZZAX(57,"JIZZAX","ДЖИЗАК","DZHIZAK",5),
    MIRZACHOL(58,"MIRZACHO`L","МИРЗАЧУЛЬ","MIRZACHOL",5),
    PAXTAKOR(59,"PAXTAKOR","ПАХТАКОР","PAXTAKOR",5),
    YANGIOBOD(60,"YANGIOBOD","ЯНГИАБАД","YANGIOBOD",5),
    ZAFAROBOD(61,"ZAFAROBOD","ЗАФАРАБАД","ZAFAROBOD",5),
    ZARBAND(62,"ZARBAND","ЗАРБДАР","ZARBAND",5),
    ZOMIN(63,"ZOMIN","ЗААМИН","ZOMIN",5),
    //KASHKADARYA
    CHIROQCHI(64,"CHIROQCHI","ЧИРАКЧИ","CHIROQCHI",6),
    DEHQONOBOD(65,"DEHQONOBOD","ДЕХКАНАБАД","DEHQONOBOD",6),
    GUZOR(66,"G`UZOR","ГУЗАР","GUZOR",6),
    KASBI(67,"KASBI","КАСБИЙ","KASBI",6),
    KITOB(68,"KITOB","КИТАБ","KITOB",6),
    KOSON(69,"KOSON","КАСАН","KOSON",6),
    MIRISHKOR(70,"MIRISHKOR","МИРИШКОР","MIRISHKOR",6),
    MUBORAK(71,"MUBORAK","МУБАРЕК","MUBORAK",6),
    NISHON(72,"NISHON","НИШАН","NISHON",6),
    QAMASHI(73,"QAMASHI","КАМАШИ","QAMASHI",6),
    QARSHI(74,"QARSHI","КАРШИ","QARSHI",6),
    SHAHRISABZ(75,"SHAHRISABZ","ШАХРИСАБЗ","SHAHRISABZ",6),
    YAKKABOG(76,"YAKKABOG`","ЯККАБАГ","YAKKABOG",6),
    //NAVOI
    KARMANA(77,"KARMANA","КАРМАН","KARMANA",7),
    KONIMEX(78,"KONIMEX","КАНИМЕХ","KONIMEX",7),
    NAVBAHOR(79,"NAVBAHOR","НАВБАХОР","NAVBAHOR",7),
    NUROTA(80,"NUROTA","НУРАТИН","NUROTA",7),
    QIZILTEPA(81,"QIZILTEPA","КЫЗЫЛТЕПА","QIZILTEPA",7),
    TOMDI(82,"TOMDI","ТАМДЫ","TOMDI",7),
    UCHQUDUQ(83,"UCHQUDUQ","УЧКУДУК","UCHQUDUQ",7),
    XATIRCHI(84,"XATIRCHI","ХАТЫРЧИ","XATIRCHI",7),
    //NAMANGAN
    CHORTOQ(85,"CHORTOQ","ЧАРТАК","CHORTOQ",8),
    CHUST(86,"CHUST","ЧУСТ","CHUST",8),
    KOSONSOY(87,"KOSONSOY","КАСАНСАЙ","KOSONSOY",8),
    MINGBULOQ(88,"MINGBULOQ","МИНГБУЛАК","MINGBULOQ",8),
    NAMANGON(89,"NAMANGAN","НАМАНГАН","NAMANGAN",8),
    NORIN(90,"NORIN","НАРЫН","NORIN",8),
    POP(91,"POP","ПАП","POP",8),
    TORAQORGON(92,"TO`RAQO`RG`ON","ТУРАКУРГАН","TORAQORGON",8),
    UCHQORGON(93,"UCHQO`RG`ON","УЧКУРГАН","UCHQORGON",8),
    UYCHI(94,"UYCHI","УЙЧИ","UYCHI",8),
    YANGIQORGON(95,"YANGIQO`RGON","ЯНГИКУРГАН","YANGIQORGOT",8),
    //SAMARKAND
    BULUNGUR(96,"BULUNG`UR","БУЛУНГУР","BULUNGUR",9),
    ISHTIXON(97,"ISHTIXON","ИШТЫХАН","ISHTIXON",9),
    JOMBOY(98,"JOMBOY","ДЖАМБАЙ","JOMBOY",9),
    KATTAQORGON(99,"KATTAQO`RG`ON","КАТТАКУРГАН","KATTAQORGON",9),
    NARPAY(100,"NARPAY","НАРПАЙ","NARPAY",9),
    NUROBOD(101,"NUROBOD","НУРАБАД","NUROBOD",9),
    OQDARYO(102,"OQDARYO","АКДАРЬЯ","OQDARYO",9),
    PAST_DARGOM(103,"PAST DARG`OM","ПАСТДАРГОМ","PAST DARGOM",9),
    PAXTACHI(104,"PAXTACHI","ПАХТАЧИ","PAXTACHI",9),
    POYARIQ(105,"POYARIQ","ПАЙАРЫК","POYARIQ",9),
    QOSHRABOT(106,"QO`SHRABOT","КОШРАБАД","QOSHRABOT",9),
    SAMARQAND(107,"SAMARQAND","САМАРКАНД","SAMARQAND",9),
    TOYLOQ(108,"TOYLOQ","ТАЙЛАК","TOYLOQ",9),
    URGUT(109,"URGUT","УРГУТ","URGUT",9),
    //SURKHANDARYA
    ANGOR(110,"ANGOR","АНГОР","ANGOR",10),
    BANDIXON(111,"BANDIXON","БАНДИХАН","BANDIXON",10),
    BOYSUN(112,"BOYSUN","БАЙСУН","BOYSUN",10),
    DENOV(113,"DENOV","ДЕНАУ","DENOV",10),
    JARQORGON(114,"JARQO`RG`ON","ДЖАРКУРГАН","JARQORGON",10),
    MUZROBOR(115,"MUZROBOR","МУЗРАБАД","MUZROBOR",10),
    OLTINSOY(116,"OLTINSOY","АЛТЫНСАЙ","OLTINSOY",10),
    QIZIRIQ(117,"QIZIRIQ","КИЗИРИК","QIZIRIQ",10),
    QUMQORGON(118,"QUMQO`RG`ON","КУМКУРГАН","QUMQORGON",10),
    SARIOSIYO(119,"SARIOSIYO","САРИАЗИЯ","SARIOSIYO",10),
    SHEROBOD(120,"SHEROBOD","ШЕРАБАД","SHEROBOD",10),
    SHORCHI(121,"SHO`RCHI","ШУРЧИ","SHORCHI",10),
    TERMIZ(122,"TERMIZ","ТЕРМЕЗ","TERMIZ",10),
    UZUN(123,"UZUN","УЗУН","UZUN",10),
    //SYRDARYA
    BOYOVUT(124,"BOYOVUT","БАЯУТ","BOYOVUT",11),
    GULISTON(125,"GULISTON","ГУЛИСТАН","GULISTON",11),
    OQOLTIN(126,"OQOLTIN","АКАЛТЫН","OQOLTIN",11),
    SARDOBA(127,"SARDOBA","САРДОБА","SARDOBA",11),
    SAYXUNOBOD(128,"SAYXUNOBOD","САЙХУНАБАД","SAYXUNOBOD",11),
    SIRDARYO(129,"SIRDARYO","СЫРДАРЬЯ","SIRDARYO",11),
    XOVOS(130,"XOVOS","ХАВАС","XOVOS",11),
    MIRZAOBOD(131,"MIRZAOBOD","МИРЗААБАД","MIRZAOBOD",11),
    //FERGHANA
    BESHARIQ(132,"BESHARIQ","БЕШАРЫК","BESHARIQ",12),
    BOGDOD(133,"BOG`DOD","БАГДАД","BOGDOD",12),
    BUVAYDA(134,"BUVAYDA","БУВАЙДА","BUVAYDA",12),
    DANGARA(135,"DANG`ARA","ДАНГАРА","DANGARA",12),
    FARGONA(136,"FARG`ONA","ФЕРГАНА","FARGONA",12),
    FURQAT(137,"FURQAT","ФУРКАТ","FURQAT",12),
    OZBEKISTON(138,"O`ZBEKISTON","УЗБЕКИСТАН","UZBEKISTAN",12),
    OLTIARIQ(139,"OLTIARIQ","АЛТЫАРЫК","OLTIARIQ",12),
    QOSHTEPA(140,"QOSHTEPA","КУШТЕПА","QOSHTEPA",12),
    QUVA(141,"QUVA","КУВА","QUVA",12),
    RISHTON(142,"RISHTON","РИШТАН","RISHTON",12),
    SOX(143,"SO`X","СОХ","SOX",12),
    TOSHLOQ(144,"TOSHLOQ","ТАШЛАК","TOSHLOQ",12),
    UCHKOPRIK(145,"UCHKO`PRIK","УЧКУПРИН","UCHKOPRIK",12),
    YOZYOVON(146,"YOZYOVON","ЯЗЪЯВАН","YOZYOVON",12),
    //XORAZM
    BOGOT(147,"BOG`OT","БАГАТ","BOGOT",13),
    GURLAN(148,"GURLAN","ГУРЛЕН","GURLAN",13),
    QOSHKOPIR(149,"QO`SHKO`PIR","КОШКУПЫР","QOSHKOPIR",13),
    SHOVOT(150,"SHOVOT","ШАВАТ","SHOVOT",13),
    URGANCH(151,"URGANCH","УРГАНЧИ","URGANCH",13),
    XAZORASP(152,"XAZORASP","ХАЗАРАСП","XAZORASP",13),
    XIVA(153,"XIVA","ХИВА","XIVA",13),
    XONQA(154,"XONQA","ХАНКА","XONQA",13),
    YANGIARIQ(155,"YANGIARIQ","ЯНГИАРЫК","YANGIARIQ",13),
    YANGIBOZOR(156,"YANGIBOZOR","ЯНГИБАЗАР","YANGIBOZOR",13),
    //REPUBLIC_OF_KARAKALPAKSTAN
    AMUDARYO(157,"AMUDARYO","АМУДАРЬЯ","AMUDARYO",14),
    BERUNIY(158,"BERUNIY","БЕРУНИЙ","BERUNIY",14),
    CHIMBOY(159,"CHIMBOY","ЧИМБАЙ","CHIMBOY",14),
    ELLIKQALA(160,"ELLIKQALA","ЭЛЛИККАЛА","ELLIKQALA",14),
    KEGEYLI(161,"KEGEYLI","КЕГЕЙЛИ","KEGEYLI",14),
    MOYNOQ(162,"MO`YNOQ","МУЙНАК","MOYNOQ",14),
    NUKUS(163,"NUKUS","НУКУС","NUKUS",14),
    QONLIKOL(164,"QONLIKO`L","КАНЛЫКУЛЬ","QONLIKOL",14),
    QORAUZAQ(165,"QORAUZAQ","КАРАУЗЯК","QORAUZAQ",14),
    QUNGIROT(166,"QUNG`IROT","КУНГРАД","QUNGIROT",14),
    SHUMANAY(167,"SHUMANAY","ШУМАНАЙ","SHUMANAY",14),
    TAXTAKOPIR(168,"TAXTAKO`PIR","ТАХТАКУПЫР","TAXTAKOPIR",14),
    TORTKOL(169,"TO`RTKO`L","ТУРТКУЛЬ","TORTKOL",14),
    XOJAYLI(170,"XO`JAYLI","ХОДЖАЙЛИ","XOJAYLI",14);


    private int id;
    private String uz;
    private String ru;
    private String eng;
    private int regionId;

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

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }
}
