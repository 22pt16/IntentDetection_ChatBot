package org.example;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatbotModel {
    private StanfordCoreNLP pipeline;
    private NaiveBayes classifier;
    private Instances trainingData;
    private Map<String, List<String>> quoteMap; // Changed to List<String> for multiple quotes per intent

    public ChatbotModel() throws Exception {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment");
        this.pipeline = new StanfordCoreNLP(props);

        quoteMap = new HashMap<>(); // Proper initialization
        loadQuotes();
        loadTrainingData();
    }

    private void loadQuotes() throws Exception {
        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("C:\\Users\\Admin\\IdeaProjects\\Chat\\src\\main\\resources\\quotes.csv"));
            Instances quoteData = loader.getDataSet();

            System.out.println("Number of quotes loaded: " + quoteData.numInstances());

            for (int i = 0; i < quoteData.numInstances(); i++) {
                String quote = quoteData.instance(i).stringValue(0).trim().replaceAll("[^a-zA-Z0-9\\s]", "");
                String intent = quoteData.instance(i).stringValue(1).trim().toLowerCase();

                System.out.println("Loading Quote: Intent = " + intent + ", Quote = " + quote);

                quoteMap.computeIfAbsent(intent, k -> new ArrayList<>()).add(quote); // Add quotes to the list
            }

            System.out.println("Quote Map Size: " + quoteMap.size());

        } catch (IOException e) {
            throw new Exception("Error loading quotes CSV file: " + e.getMessage());
        }
    }

    public String getRandomQuote() throws Exception {
        ArrayList<Object> quotes = new ArrayList<>();
        try {
            // Read all lines from the CSV file
            List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\Admin\\IdeaProjects\\Chat\\src\\main\\resources\\quotes.csv"));
            for (String line : lines) {
                // Assuming each line contains a quote
                quotes.add(line.trim());
            }
        } catch (IOException e) {
            throw new Exception("Error loading quotes CSV file: " + e.getMessage());
        }
        Random rand = new Random();
        return (String) quotes.get(rand.nextInt(quotes.size()));
    }

    private void loadTrainingData() throws Exception {
        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("C:\\Users\\Admin\\IdeaProjects\\Chat\\src\\main\\resources\\intent.csv"));
            trainingData = loader.getDataSet();
            trainingData.setClassIndex(trainingData.numAttributes() - 1);

            classifier = new NaiveBayes();
            classifier.buildClassifier(trainingData);
        } catch (IOException e) {
            throw new Exception("Error loading intent CSV file: " + e.getMessage());
        }
    }

    public String openChat() {
        return "Welcome to Mental Support Bot! \n" +
                "I'm here to listen and offer support. Please type 'help' for more information or ask a question to get started!";
    }

    public String getBestMatchingIntent(String input) throws Exception {
        String lowerCaseInput = input.toLowerCase().trim();
        String bestMatch = null;
        double highestSimilarity = 0.0;

        for (int i = 0; i < trainingData.numInstances(); i++) {
            String trainingSentence = trainingData.instance(i).stringValue(0).toLowerCase();
            double similarity = computeSimilarity(lowerCaseInput, trainingSentence);

            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatch = trainingData.instance(i).stringValue(1).toLowerCase(); // Ensure case consistency
            }
        }

        // Handle greetings
        if (lowerCaseInput.contains("hi") || lowerCaseInput.contains("hey") || lowerCaseInput.contains("hello")) {
            return "greeting";
        }

        return bestMatch;
    }

    private double computeSimilarity(String input, String trainingSentence) {
        Annotation annotation = new Annotation(input);
        pipeline.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        List<String> inputTokens = new ArrayList<>();
        for (CoreMap sentence : sentences) {
            for (HasWord token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                inputTokens.add(token.word());
            }
        }

        Set<String> inputTokenSet = new HashSet<>(inputTokens);
        Set<String> trainingTokenSet = new HashSet<>(Arrays.asList(trainingSentence.split("\\s+")));

        Set<String> intersection = new HashSet<>(inputTokenSet);
        intersection.retainAll(trainingTokenSet);

        Set<String> union = new HashSet<>(inputTokenSet);
        union.addAll(trainingTokenSet);

        return (double) intersection.size() / union.size();
    }

    public String getSentiment(String input) {
        Annotation annotation = new Annotation(input);
        pipeline.annotate(annotation);
        String sentiment = "";

        for (var sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
        }
        return sentiment;
    }

    public String processInput(String input, String mood) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Runnable detectionRunnable = new Runnable() {
            @Override
            public void run() {
                String intent = null;
                try {
                    intent = getBestMatchingIntent(input).toLowerCase().trim();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String sentiment = getSentiment(input);

                System.out.println("\nSentiment: " + sentiment);
                System.out.println("Intent: " + intent);

                if (intent == null) {
                    System.out.println("No matching intent found for input: " + input);
                    System.out.println("Sorry, I didn't understand that.");
                } else {
                    System.out.println("Found intent in quoteMap: " + intent);

                }
            }
        };

        executor.execute(detectionRunnable);
        executor.shutdown();

        return generateResponse(getBestMatchingIntent(input).toLowerCase().trim());
    }

    private String generateResponse(String intent) {
        if (intent == null) {
            return "Sorry, I didn't understand that.";
        }
        if (intent.equals("greeting")) {
            return "Hey, how can I help you?";
        }
        List<String> responses = quoteMap.get(intent);
        if (responses != null && !responses.isEmpty()) {
            return responses.get(new Random().nextInt(responses.size())); // Random response from the list
        } else {
            return "Sorry, I couldn't find a response for that.";
        }
    }
}