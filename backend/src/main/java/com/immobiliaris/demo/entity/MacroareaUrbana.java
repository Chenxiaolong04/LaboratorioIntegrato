package com.immobiliaris.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MacroareaUrbana")
public class MacroareaUrbana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true)
    private String nome_macroarea;
    private Integer NTN;
    private Double NTN_var;
    private Double IMI;
    private Double IMI_diff;
    private Double quota_NTN;
    private Double quotazione_media;
    private Double quotazione_var;

    @Column(name = "CAP_da")
    private String cap_da;

    @Column(name = "CAP_a")
    private String cap_a;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNome_macroarea() { return nome_macroarea; }
    public void setNome_macroarea(String nome_macroarea) { this.nome_macroarea = nome_macroarea; }
    
    public Integer getNTN() { return NTN; }
    public void setNTN(Integer NTN) { this.NTN = NTN; }
    
    public Double getNTN_var() { return NTN_var; }
    public void setNTN_var(Double NTN_var) { this.NTN_var = NTN_var; }
    
    public Double getIMI() { return IMI; }
    public void setIMI(Double IMI) { this.IMI = IMI; }
    
    public Double getIMI_diff() { return IMI_diff; }
    public void setIMI_diff(Double IMI_diff) { this.IMI_diff = IMI_diff; }
    
    public Double getQuota_NTN() { return quota_NTN; }
    public void setQuota_NTN(Double quota_NTN) { this.quota_NTN = quota_NTN; }
    
    public Double getQuotazione_media() { return quotazione_media; }
    public void setQuotazione_media(Double quotazione_media) { this.quotazione_media = quotazione_media; }
    
    public Double getQuotazione_var() { return quotazione_var; }
    public void setQuotazione_var(Double quotazione_var) { this.quotazione_var = quotazione_var; }

    // Getters e Setters
    public String getCap_da() { return cap_da; }
    public void setCap_da(String cap_da) { this.cap_da = cap_da; }

    public String getCap_a() { return cap_a; }
    public void setCap_a(String cap_a) { this.cap_a = cap_a; }
}