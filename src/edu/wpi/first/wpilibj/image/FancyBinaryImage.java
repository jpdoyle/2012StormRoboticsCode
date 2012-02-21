/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.image;

/**
 *
 * @author joe
 */
public class FancyBinaryImage extends BinaryImage {
    private int numParticles;
    public FancyBinaryImage() throws NIVisionException {
        super();
    }

    public int getNumberParticles () throws NIVisionException{
        numParticles = NIVision.countParticles(image);
        return numParticles;
    }

    public ParticleAnalysisReport getParticleAnalysisReport(int index) throws NIVisionException{
        if(index >= numParticles) throw new IndexOutOfBoundsException("Invalid particle index");
        return new ParticleAnalysisReport(this, index);
    }
}
