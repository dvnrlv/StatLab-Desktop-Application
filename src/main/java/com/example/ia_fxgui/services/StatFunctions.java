package com.example.ia_fxgui.services;

import java.time.LocalDate;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.*;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.ArrayList;

public class StatFunctions {




    private void StatFunctions(){}

    private static DescriptiveStatistics stats = new DescriptiveStatistics();
    private static ArrayList<ArrayList<Object>> resultList = new ArrayList<>();
    public static Object[][] resultStatArray;

    public static Double calculateYMean(Double[][] csvData) {
        Mean mean = new Mean();
        Double meanValue = mean.evaluate(getColumn(csvData, 1)); // Y values are in the second column
        addOrUpdateResult("Mean of the Dependent Variable", meanValue);
        return meanValue;
    }

    public static Double calculateYStandardDeviation(Double[][] csvData) {
        StandardDeviation stdDev = new StandardDeviation();
        Double stdDevValue = stdDev.evaluate(getColumn(csvData, 1)); // Y values are in the second column
        addOrUpdateResult("Standard Deviation of the Dependent Variable", stdDevValue);
        return stdDevValue;
    }

    public static Double calculateYMedian(Double[][] csvData) {
        Median median = new Median();
        Double medianValue = median.evaluate(getColumn(csvData, 1)); // Y values are in the second column
        addOrUpdateResult("Median of the Dependent Variable", medianValue);
        return medianValue;
    }

    public static Double calculateYVariance(Double[][] csvData) {
        Variance variance = new Variance();
        Double varianceValue = variance.evaluate(getColumn(csvData, 1)); // Y values are in the second column
        addOrUpdateResult("Variance of the Dependent Variable", varianceValue);
        return varianceValue;
    }


    public static Double calculateMinY(Double[][] csvData) {
        Min min = new Min();
        Double minY = min.evaluate(getColumn(csvData, 1)); // Y values are in the second column
        addOrUpdateResult("Minimum of the Dependent Variable", minY);
        return minY;
    }

    public static Double calculateMaxY(Double[][] csvData) {
        Max max = new Max();
        Double maxY = max.evaluate(getColumn(csvData, 1)); // Y values are in the second column
        addOrUpdateResult("Max of the Dependent Variable", maxY);
        return maxY;
    }

    public static Double calculateKurtosisY(Double[][] csvData) {
        Kurtosis kurtosis = new Kurtosis();
        Double kurtosisY = kurtosis.evaluate(getColumn(csvData, 1)); // Y values are in the second column
        addOrUpdateResult("Kurtosis of the Dependent Variable", kurtosisY);
        return kurtosisY;
    }

    public static Double calculateSkewnessY(Double[][] csvData) {
        Skewness skewness = new Skewness();
        Double skewnessY = skewness.evaluate(getColumn(csvData, 1)); // Y values are in the second column
        addOrUpdateResult("Skewness of the dependent variable", skewnessY);
        return skewnessY;
    }

    public static Double calculateCovarianceXY(Double[][] csvData) {
        Covariance covariance = new Covariance();
        Double covarianceXY = covariance.covariance(getColumn(csvData, 0), getColumn(csvData, 1)); // X and Y values are in the first and second columns, respectively
        addOrUpdateResult("Covariance", covarianceXY);
        return covarianceXY;
    }

    public static Double calculatePearsonsCorrelationCoefficient(Double[][] csvData) {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        Double pearsonsCorrelation = correlation.correlation(getColumn(csvData, 0), getColumn(csvData, 1)); // X and Y values are in the first and second columns, respectively
        addOrUpdateResult("Pearson's Correlation Coefficient", pearsonsCorrelation);
        return pearsonsCorrelation;
    }

    public static Double calculateSpearmansCorrelationCoefficient(Double[][] csvData) {
        SpearmansCorrelation correlation = new SpearmansCorrelation();
        Double spearmansCorrelation = correlation.correlation(getColumn(csvData, 0), getColumn(csvData, 1)); // X and Y values are in the first and second columns, respectively
        addOrUpdateResult("Spearman's Correlation Coefficient", spearmansCorrelation);
        return spearmansCorrelation;
    }

    public static Double calculateKendallsCorrelationCoefficient(Double[][] csvData) {
        KendallsCorrelation correlation = new KendallsCorrelation();
        Double kendallsCorrelation = correlation.correlation(getColumn(csvData, 0), getColumn(csvData, 1)); // X and Y values are in the first and second columns, respectively
        addOrUpdateResult("Kendall's Correlation Coefficient", kendallsCorrelation);
        return kendallsCorrelation;
    }


    public static String fitPolynomial(int n, Double[][] csvData) {

        if(n!=0){
        WeightedObservedPoints obs = new WeightedObservedPoints();
        for (Double[] row : csvData) {
            obs.add(row[0], row[1]); // Assuming X values are in the first column, Y values in the second
        }

        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(n);
        double[] coeffs = fitter.fit(obs.toList());

        PolynomialFunction polynomialFunction = new PolynomialFunction(roundCoefficients(coeffs));

        addOrUpdateResult("Polynomial Curve fit of degree "+n, polynomialToString(polynomialFunction));
        return polynomialToString(polynomialFunction);

        }
        else{
            return null;
        }
    }

    public static void createResultArray(String fileName) {
        resultStatArray = new Object[resultList.size() + 2][2]; // Adding 2 rows for filename and file date
        resultStatArray[0][0] = "Filename"; // Placeholder for filename
        resultStatArray[0][1] = fileName; // Set filename
        resultStatArray[1][0] = "File Date"; // Placeholder for file date
        resultStatArray[1][1] = LocalDate.now().toString(); // Set current date
        for (int i = 0; i < resultList.size(); i++) {
            resultStatArray[i + 2][0] = resultList.get(i).get(0);
            resultStatArray[i + 2][1] = resultList.get(i).get(1);
        }
        clearStats();
    }

    public static void clearResultArray() {
        resultList.clear();
        resultStatArray = null;
    }

    private static double[] roundCoefficients(double[] coefficients) {
        double[] roundedCoefficients = new double[coefficients.length];
        for (int i = 0; i < coefficients.length; i++) {
            roundedCoefficients[i] = roundToThreeSF(coefficients[i]);
        }
        return roundedCoefficients;
    }

    private static String polynomialToString(PolynomialFunction polynomialFunction) {
        StringBuilder sb = new StringBuilder();
        double[] coefficients = polynomialFunction.getCoefficients();
        int degree = coefficients.length - 1;

        for (int i = degree; i >= 0; i--) {
            Double coefficient = roundToThreeSF(coefficients[i]);
            if (coefficient != 0) {
                if (i == degree) {
                    sb.append(coefficient);
                } else {
                    sb.append(coefficient > 0 ? "+" : "-");
                    sb.append(Math.abs(coefficient));
                }
                if (i > 0) {
                    sb.append("x");
                    if (i > 1) {
                        sb.append("^").append(i);
                    }
                }
            }
        }

        return sb.toString();
    }


    private static Double roundToThreeSF(Double value) {
        Double scale = Math.pow(10, 3 - Math.floor(Math.log10(Math.abs(value))));
        return Math.round(value * scale) / scale;
    }

    private static double[] getColumn(Double[][] csvData, int columnIndex) {
        double[] column = new double[csvData.length];
        for (int i = 0; i < csvData.length; i++) {
            column[i] = csvData[i][columnIndex];
        }
        return column;
    }

    private static void addOrUpdateResult(String functionName, Object value) {
        // Check if the function already exists in the resultList
        for (ArrayList<Object> row : resultList) {
            if (row.get(0).equals(functionName)) {
                // Update the existing value
                row.set(1, value);
                return;
            }
        }
        // If the function does not exist, add it as a new row
        ArrayList<Object> newRow = new ArrayList<>();
        newRow.add(functionName);
        newRow.add(value);
        resultList.add(newRow);
    }

    private static void clearStats() {
        stats.clear();
        resultStatArray = null;
    }

}
