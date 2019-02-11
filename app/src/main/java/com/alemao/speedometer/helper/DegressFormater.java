package com.alemao.speedometer.helper;

public class DegressFormater {
    static public String format(double d){
        String res;
        int aux, val = (int)d;   //pega apenas a parte inteira de d
        res = ""+val+""+((char)0x00B0)+" ";

        d-=val; //remove a parte inteira de d
        d*=60;  //passa o d para minutos
        val = (int)d;

        res += val+"\' ";

        d-=val; //remove a parte inteira de d
        d*=60;  //passa o d para segundos
        val = (int)d;

        res += val+"\" ";

        return res;
    }
}
