package dev.colbster937.eaglercraft;

public class EaglerMathHelper {
  private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15,
      25, 17, 4, 8, 31, 27,
      13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };

  public static double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }

  public static int clamp(int value, int min, int max) {
    return (int) clamp(value, min, max);
  }

  private static boolean isPowerOfTwo(int value) {
    return value != 0 && (value & value - 1) == 0;
  }

  private static int smallestEncompassingPowerOfTwo(int value) {
    int i = value - 1;
    i = i | i >> 1;
    i = i | i >> 2;
    i = i | i >> 4;
    i = i | i >> 8;
    i = i | i >> 16;
    return i + 1;
  }

  public static int log2DeBruijn(int value) {
    value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
    return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) ((long) value * 125613361L >> 27) & 31];
  }
}
