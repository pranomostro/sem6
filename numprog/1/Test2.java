public class Test2 {
    public static void main(String[] args) {
        /*
        Gleitpunktzahl.setSizeMantisse(4);
        Gleitpunktzahl.setSizeExponent(2);
        Gleitpunktzahl g1 = new Gleitpunktzahl(0.4);
        System.out.println(g1.toString());
        */
        Gleitpunktzahl.setSizeMantisse(24);
        Gleitpunktzahl.setSizeExponent(8);
        Gleitpunktzahl g = new Gleitpunktzahl(0.5);
        System.out.println(FastMath.invSqrt(g));
    }
}
