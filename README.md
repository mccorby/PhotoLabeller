# Federated Learning
This project is an implementation of a [Federated Learning](https://research.googleblog.com/2017/04/federated-learning-collaborative.html) system consisting on a [Parameter Server](https://github.com/mccorby/PhotoLabellerServer) and an [Android application](https://github.com/mccorby/PhotoLabeller) that can be used as a client

Both components are implemented in Kotlin using DL4J as the Machine Learning framework


Enjoy!

## The problem
Training a machine learning model requires data. The more we have, the better (well... not always, but let's allow some simplifications). However, data is not cheap and more importantly, it can contain sensitive and personal information.

Recent developments in privacy in the form of new laws as [GDPR](https://www.eugdpr.org/) and the increase of awareness of users and citizens in the value of their data is generating a need for techniques to enforce more privacy

Though techniques as [anonymisation](https://ico.org.uk/for-organisations/guide-to-data-protection/anonymisation/) can greatly help with the privacy issue the fact that all the data is being sent to a central location to train the machine learning models is always a motive to be worried about


## Federated Learning as a solution to privacy
Federated Learning turns the update of Machine Learning models upside-down by allowing the devices on the edge to participate in the training.

Instead of sending the data in the client to a centralised location, Federated Learning sends the model to the devices participating in the federation. The model is then re-trained (using [Transfer Learning](http://ruder.io/transfer-learning/)) with the local data

And the data, your data, never leaves the device, let that be your phone, your laptop or your IoT gadget

## High-level Architecture

![image alt text](art/high_level_arch.png)

## Use Case. Classifying and training images
To demonstrate how Federated Learning works, I have implemented a system based on Cifar-10, a well-known image classification dataset

### Android client
#### Client Architecture
#### Separation of concerns
Change the UI bit in Android and you can have with little effort another type of client that supports Kotlin

### Parameter Server
#### Server Architecture
#### Federated Averaging

## References
* [Federated Learning](https://research.googleblog.com/2017/04/federated-learning-collaborative.html)
* [OpenMined](https://www.openmined.org/)