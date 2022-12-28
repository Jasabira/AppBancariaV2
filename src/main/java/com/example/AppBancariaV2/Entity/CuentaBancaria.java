package com.example.AppBancariaV2.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CuentaBancaria")
public class CuentaBancaria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int numeroDeTarjeta;
    private String usuario;
    private String pass;
    private int monto;

    public CuentaBancaria() {
    }

    public CuentaBancaria(String usuario, String pass) {
        this.id = null;
        this.numeroDeTarjeta = (int) (Math.random() * 100000000 + 1);
        this.usuario = usuario;
        this.pass = pass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumeroDeTarjeta() {
        return numeroDeTarjeta;
    }

    public void setNumeroDeTarjeta(int numeroDeTarjeta) {
        this.numeroDeTarjeta = numeroDeTarjeta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "CuentaBancaria{" +
                "id=" + id +
                ", numeroDeTarjeta=" + numeroDeTarjeta +
                ", usuario='" + usuario + '\'' +
                ", password='" + pass + '\'' +
                ", monto=" + monto +
                '}';
    }
}
