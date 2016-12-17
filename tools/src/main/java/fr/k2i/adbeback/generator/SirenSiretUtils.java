package fr.k2i.adbeback.generator;

import org.apache.commons.lang.math.RandomUtils;

/**
 * User: dimitri
 * Date: 06/03/13
 * Time: 14:19
 * Goal:
 */
public class SirenSiretUtils {

    public String getSirenFromSiret(String siret) {
        return siret.substring(0,9);
    }

    private enum TypeGenerator{
        SIRET(4),
        SIREN(8);
        private int length;

        TypeGenerator(int l) {
            length = l;
        }

        public int getlength() {
            return length;
        }
    }

    private String generate(TypeGenerator type) {
        StringBuilder sb = new StringBuilder();
        if(TypeGenerator.SIRET.equals(type)){
            sb.append(generate(TypeGenerator.SIREN));
        }

        for (int i = 0; i < type.getlength(); i++) {
            sb.append(RandomUtils.nextInt(10));
        }

        String sirenNoBitControl = sb.toString();
        int somme = 0;
        for (int index = 0; index < sirenNoBitControl.length(); index++) {
            int value = Character.getNumericValue(sirenNoBitControl.charAt(index));

                switch (type){
                    case SIREN:
                        if (index % 2 != 0) {
                            value*=2;
                        }
                        break;
                    case SIRET:
                        if (index % 2 == 0) {
                            value*=2;
                        }
                        break;
                }

            somme += (value > 9) ? ((value - 10) + 1) : value;
        }

        if ((somme % 10) == 0) {
            sb.append(0);
        } else {
            sb.append(10 - (somme % 10));
        }

        return sb.toString();
    }

    public String sirenGenerator(){
       return  generate(TypeGenerator.SIREN);
    }


    public String siretGenerator(){
        return generate(TypeGenerator.SIRET);
    }


    public boolean isSiretNumber(String siret){
        return (siret!=null)?(siret.length() == 14):false;
    }

    public boolean isSirenNumber(String siren){
        return (siren!=null)?(siren.length() == 9):false;
    }


    public static void main(String[] args) {
        System.out.println(new SirenSiretUtils().generate(TypeGenerator.SIRET));
    }
}
