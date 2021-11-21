package com.brandsure.common;

/**
 * Data Tronsfer Object for calculated properties
 * like GS1, GTIN, GLN and SGLN
 *
 */
public class GTIN_DTO {
    String GS1;
    String GTIN;
    String GLN;
    String SGLN; // SGLN is GS1 company prefix plus . + enough zeros to make it 12 digits + .0

    public GTIN_DTO(String GS1, String GTIN, String GLN, String SGLN) {
        this.GS1 = GS1;
        this.GTIN = GTIN;
        this.GLN = GLN;
        this.SGLN = SGLN;
    }

    public String getGS1() {
        return GS1;
    }

    public String getGTIN() {
        return GTIN;
    }

    public String getGLN() {
        return GLN;
    }

    public String getSGLN() { return SGLN; }

    public String toString() {
        return "GS1:" + GS1 + ", GTIN:" +GTIN + ",GLN:" + GLN + ",SGLN:" + SGLN;
    }
}
