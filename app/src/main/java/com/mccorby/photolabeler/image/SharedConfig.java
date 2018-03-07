package com.mccorby.photolabeler.image;

import com.google.gson.annotations.SerializedName;

// TODO This class should not be polluted with Gson annotations
// TODO Provide a low level object and a mapper
public class SharedConfig {

    @SerializedName("imageSize")
    private int imageSize;
    @SerializedName("batchSize")
    private int batchSize;
    @SerializedName("modelFileName")
    private String modelFileName;
    @SerializedName("outputNodeName")
    private String outputNodeName;
    @SerializedName("outputNodeNames")
    private String[] outputNodeNames;
    @SerializedName("inputNodeName")
    private String inputNodeName;
    @SerializedName("outputSize")
    private int outputSize;

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getModelFileName() {
        return modelFileName;
    }

    public void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    public String getOutputNodeName() {
        return outputNodeName;
    }

    public void setOutputNodeName(String outputNodeName) {
        this.outputNodeName = outputNodeName;
    }

    public String getInputNodeName() {
        return inputNodeName;
    }

    public void setInputNodeName(String inputNodeName) {
        this.inputNodeName = inputNodeName;
    }

    public String[] getOutputNodeNames() {
        return outputNodeNames;
    }

    public void setOutputNodeNames(String[] outputNodeNames) {
        this.outputNodeNames = outputNodeNames;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }
}
