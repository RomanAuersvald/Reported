package com.example.securingweb.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;


class Pravni_forma {
    public int Kod_PF;
}

 class Adresa_ARES {
    public int ID_adresy;
    public int Kod_statu;
     public String Nazev_okresu;
     @XmlElement(name = "dtt:Nazev_obce")
    public String Nazev_obce;
    public String Nazev_casti_obce;
    public String Nazev_mestske_casti;
     @XmlElement(name = "dtt:Nazev_ulice")
    public String Nazev_ulice;
     @XmlElement(name = "dtt:Cislo_domovni")
    public int Cislo_domovni;
    public int Typ_cislo_domovni;
    public String Cislo_orientacni;
     @XmlElement(name = "dtt:PSC")
    public int PSC;
}

 class Identifikace {
     @XmlElement(name = "are:Adresa_ARES")
    public Adresa_ARES Adresa_ARES;
}
//@XmlRootElement(name = "Zaznam")
 class Zaznam {
    public String Vyhledano_dle;
    public Date Datum_vzniku;
    public Date Datum_platnosti;
    @XmlElement(name = "are:Pravni_forma")
    public Pravni_forma Pravni_forma;
    @XmlElement(name = "are:Obchodni_firma")
    public String Obchodni_firma;
    @XmlElement(name = "are:ICO")
    public int ICO;
    @XmlElement(name = "are:Identifikace")
    public Identifikace Identifikace;
    public int Kod_FU;
    public String Priznaky_subjektu;
}

//@XmlRootElement(name = "Odpoved")
class Odpoved{
    @XmlElement(name = "are:Pocet_zaznamu")
    public int Pocet_zaznamu;
    @XmlElement(name = "are:Typ_vyhledani")
    public String Typ_vyhledani;
    @XmlElement(name = "are:Zaznam")
    public Zaznam Zaznam;
}
@XmlRootElement(name = "are:Ares_odpovedi")
public class ARESClient {
    public List<Object> script;
    @XmlElement(name = "are:Odpoved")
    public Odpoved Odpoved;
    public Date odpoved_datum_cas;
    public int odpoved_pocet;
    public String text;

    public String getICO(){
        return String.valueOf(this.Odpoved.Zaznam.ICO);
    }

    public String getCompanyName(){
        return this.Odpoved.Zaznam.Obchodni_firma;
    }
    public Integer getPocetZaznamu(){
        return this.Odpoved.Pocet_zaznamu;
    }
    public String getStreetName(){
        return this.Odpoved.Zaznam.Identifikace.Adresa_ARES.Nazev_ulice;
    }
    public Integer getPSC(){
        return this.Odpoved.Zaznam.Identifikace.Adresa_ARES.PSC;
    }
    public String getCityName(){
        return this.Odpoved.Zaznam.Identifikace.Adresa_ARES.Nazev_obce;
    }

}

