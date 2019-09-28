package com.example.lenovog50.pedometre2;

public class IMCUtils {

    public static float getIMC (int weight,int height)
    {
        float h = (float)height/100;
        float imc = (float) weight/(h*h);
        return imc;
    }

    public static String getRmq (float imc)
    {
        if (18.5<imc && imc<24.9)
        {
            return "Poids normal";
        }
        if (imc >= 24.9 && imc < 29.9)
        {
            return "Surpoids";
        }
        if (imc >= 29.9 && imc < 40)
        {
            return "Obésité";
        }
        if (imc >=40)
        {
            return "Obésité massive";
        }
        if (imc <= 18.5)
        {
            return "Maigreur";
        }
        return "Maigreur";
    }
}
