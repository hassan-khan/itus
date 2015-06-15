/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uwaterloo.crysp.itus.utils;

/*************************************************************************
source: http://introcs.cs.princeton.edu/java/97data/Complex.java.html
Copyright © 2000–2011, Robert Sedgewick and Kevin Wayne
*************************************************************************/

/**
 * Complex number class 
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class ComplexNumbers {
	
    /**
     * The real part
     */
    private final double re;   // the real part
    /**
     * The Imaginary part
     */
    private final double im;   // the imaginary part

    /**
     * create a new object with the given real and imaginary parts
     * @param real the real part
     * @param imag the imaginary part
     */
    public ComplexNumbers(double real, double imag) {
        re = real;
        im = imag;
    }

    // return a string representation of the invoking Complex object
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    
    /**
     * return abs/modulus/magnitude
     * @return absolute as a double
     */
    public double abs()   { 
    	return Math.hypot(re, im); 
    }  // Math.sqrt(re*re + im*im)
    
    /**
     * and angle/phase/argument
     * @return angle as a double
     */
    public double phase() { 
    	return Math.atan2(im, re); 
    }  // between -pi and pi

    
    /**
     * return a new Complex object whose value is (this + b)
     * @param b a complex number
     * @return new complex number
     */
    public ComplexNumbers plus(ComplexNumbers b) {
        ComplexNumbers a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new ComplexNumbers(real, imag);
    }

    /**
     * return a new Complex object whose value is (this - b)
     * @param b a complex number
     * @return new complex number
     */
    public ComplexNumbers minus(ComplexNumbers b) {
        ComplexNumbers a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new ComplexNumbers(real, imag);
    }

    /**
     * return a new Complex object whose value is (this * b)
     * @param b a complex number
     * @return new complex number
     */
    public ComplexNumbers times(ComplexNumbers b) {
        ComplexNumbers a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new ComplexNumbers(real, imag);
    }

    /**
     * scalar multiplication
     * @param alpha scalar component
     * @return return a new object whose value is (this * alpha)
     */
    public ComplexNumbers times(double alpha) {
        return new ComplexNumbers(alpha * re, alpha * im);
    }

     
    /**
     * Return conjugate of this complex number
     * @return a new Complex object whose value is the conjugate of this
     */
    public ComplexNumbers conjugate() {  return new ComplexNumbers(re, -im); }

    /**
     * Return reciprocal of this complex number
     * @return return a new Complex object whose value is the reciprocal of this
     */
    public ComplexNumbers reciprocal() {
        double scale = re*re + im*im;
        return new ComplexNumbers(re / scale, -im / scale);
    }

    // return the real or imaginary part
    /**
     * Return the real part of this ComplexNumber
     * @return the real part
     */
    public double re() {
    	return re; 
    }
    
    /**
     * Return the imaginary part of this ComplexNumber
     * @return the imaginary part
     */
    public double im() { 
    	return im; 
    }

    
    /**
     * return a / b
     * @param b a ComplexNumber
     * @return ComplexNumber a/b
     */
    public ComplexNumbers divides(ComplexNumbers b) {
        ComplexNumbers a = this;
        return a.times(b.reciprocal());
    }

    /**
     * return a new Complex object whose value is the complex exponential of 
     * this
     * @return a new Complex object whose value is the complex exponential 
     * of this
     */
    public ComplexNumbers exp() {
        return new ComplexNumbers(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    
    /**
     * return a new Complex object whose value is the complex sine of this
     * @return  a new Complex object whose value is the complex sine of 
     * this
     */
    public ComplexNumbers sin() {
        return new ComplexNumbers(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    
    /**
     * return a new Complex object whose value is the complex cosine of this
     * @return a new Complex object whose value is the complex cosine of 
     * this
     */
    public ComplexNumbers cos() {
        return new ComplexNumbers(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    
    /**
     * return a new Complex object whose value is the complex tangent of this
     * @return a new Complex object whose value is the complex tangent of this
     */
    public ComplexNumbers tan() {
        return sin().divides(cos());
    }
    
    
    /**
     * a static version of plus
     * @param a a ComplexNumber
     * @param b a ComplexNumber
     * @return sum of two ComplexNumbers
     */
    public static ComplexNumbers plus(ComplexNumbers a, ComplexNumbers b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        ComplexNumbers sum = new ComplexNumbers(real, imag);
        return sum;
    }
    
	
	/**
	 * circ_r computation. Translated from www.kyb.mpg.de/~berens/circStat.html
	 * @param arr list of doubles
	 * @return circ_r of arr
	 */
	public static double circ_r(double []arr) {
		ComplexNumbers temp = new ComplexNumbers(0, 1);
		ComplexNumbers c = temp.times(arr[0]).exp();
		for (int i = 1; i < arr.length; i++)
			c.plus(temp.times(arr[i]).exp());
		return c.abs()/arr.length;
	}

	
	/**
	 * circ_mean computation. 
	 * Translated from www.kyb.mpg.de/~berens/circStat.html
	 * @param arr list of doubles
	 * @return circ_mean of arr
	 */
	public static double circ_mean(double []arr) {
		ComplexNumbers temp = new ComplexNumbers(0, 1);
		ComplexNumbers c = temp.times(arr[0]).exp();
		for (int i = 1; i < arr.length; i++)
			c.plus(temp.times(arr[i]).exp());
		return Math.atan2(c.im(), c.re());
	}

}