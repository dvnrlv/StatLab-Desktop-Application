package com.example.ia_fxgui.services;

import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.*;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.stat.descriptive.rank.Min;

import java.util.ArrayList;
import java.util.List;

public class StatFunctions {


//    private void StatFunctions(){}


    private static DescriptiveStatistics stats = new DescriptiveStatistics();
    private static List<StatFunctionRow> resultList = new ArrayList<>();


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

        if (n != 0) {
            WeightedObservedPoints obs = new WeightedObservedPoints();
            for (Double[] row : csvData) {
                obs.add(row[0], row[1]); // Assuming X values are in the first column, Y values in the second
            }

            PolynomialCurveFitter fitter = PolynomialCurveFitter.create(n);
            double[] coeffs = fitter.fit(obs.toList());

            PolynomialFunction polynomialFunction = new PolynomialFunction(roundCoefficients(coeffs));

            addOrUpdateResult("Polynomial Curve fit of degree " + n, polynomialToString(polynomialFunction));
            return polynomialToString(polynomialFunction);
        } else {
            return null;
        }
    }

    public static List<StatFunctionRow> createResultArray(String fileName) {
        List<StatFunctionRow> resultStatList = new ArrayList<>();
        resultStatList.add(new StatFunctionRow("FileName", fileName));
        resultStatList.addAll(resultList);
        return resultStatList;
//        clearStats();
    }

    public static void clearResultArray() {
        resultList.clear();
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
        for (StatFunctionRow row : resultList) {
            if (row.getName().equals(functionName)) {
                // Update the existing value
                if (value instanceof Double) {

                    row.setValue(value.toString());
                }
                return;
            }
        }
        // If the function does not exist, add it as a new row
        StatFunctionRow newRow = new StatFunctionRow(functionName, value.toString());
        resultList.add(newRow);
    }

    private static void clearStats() {
        stats.clear();
    }

    public static class StatFunctionRow {
        private SimpleStringProperty name;
        private SimpleStringProperty value;

        public StatFunctionRow(String name, String value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value);
        }

        public StatFunctionRow(String name, double value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(Double.toString(value));
        }

        public String getName() {
            return this.name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getValue() {
            return this.value.get();
        }

        public void setValue(String value) {
            this.value.set(value);
        }

        public void setValue(double value) {
            this.value.set(Double.toString(value));
        }
    }
}
