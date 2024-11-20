package org.example;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class IntentRecognitionModel {
    private NaiveBayes classifier;
    private Instances trainingData;
    private StanfordCoreNLP pipeline;

    public IntentRecognitionModel() throws Exception {
        // Load the training data from a CSV file
        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("C:\\Users\\Admin\\IdeaProjects\\Chat\\src\\main\\resources\\intent.csv"));
            trainingData = loader.getDataSet();
            if (trainingData == null) {
                throw new Exception("Unable to read CSV file");
            }
            // Set the class index to the last attribute
            trainingData.setClassIndex(trainingData.numAttributes() - 1);

            // Train the classifier
            classifier = new NaiveBayes();
            classifier.buildClassifier(trainingData);
        } catch (IOException e) {
            System.out.println("Error loading intent CSV file: " + e.getMessage());
        }

        // Initialize the sentiment analysis pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public String recognizeIntent(String input) throws Exception {
        // Analyze the sentiment of the input
        Annotation annotation = pipeline.process(input);
        String sentiment = getSentiment(annotation);

        // Create a new instance with the input text
        Instance instance = new DenseInstance(trainingData.numAttributes());
        instance.setValue(trainingData.attribute(0), input);
        instance.setDataset(trainingData);

        // Classify the input text
        double[] probabilities = classifier.distributionForInstance(instance);
        int predictedClassIndex = (int) classifier.classifyInstance(instance);
        String predictedClass = trainingData.classAttribute().value(predictedClassIndex);

        // Print the sentiment and intent
        System.out.println("Sentiment: " + sentiment);
        System.out.println("Intent: " + predictedClass);

        return predictedClass;
    }

    private String getSentiment(Annotation annotation) {
        String sentiment = "";
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentimentType = RNNCoreAnnotations.getPredictedClass(tree);
            sentiment = sentimentFromLabel(sentimentType);
        }
        return sentiment;
    }

    private String sentimentFromLabel(int label) {
        switch (label) {
            case 0:
            case 1:
                return "Very Negative";
            case 2:
                return "Negative";
            case 3:
                return "Neutral";
            case 4:
                return "Positive";
            case 5:
                return "Very Positive";
            default:
                return "";
        }
    }
}