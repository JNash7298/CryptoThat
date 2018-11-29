package com.chat.crypto.johnathannash.cryptothat.helpers;

import com.chat.crypto.johnathannash.cryptothat.models.CipherSpinnerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CipherProgram {

    private List<CipherSpinnerData> cipherNamesAndIds;
    private String cipherId;
    private HashMap<Character, Integer> characterValues;
    private HashMap<Integer, Character> byteCharacters;

    public CipherProgram(){
        retrieveCipherData();
    }

    private void retrieveCipherData(){
        cipherNamesAndIds = new ArrayList<>();
        characterValues = new HashMap<>();
        byteCharacters = new HashMap<>();

        //this is temp data
        cipherNamesAndIds.add(new CipherSpinnerData("regular", "1"));
        cipherNamesAndIds.add(new CipherSpinnerData("ceasar", "2"));
        cipherNamesAndIds.add(new CipherSpinnerData("affine", "3"));

        setupBaseAlphabet();
    }

    private void setupBaseAlphabet(){
        int capital = 65, lower = 97, number = 48;
        int amount;

        for(int character = 0; character < 26; character++){
            amount = characterValues.size();
            characterValues.put((char)(lower + character), amount);
        }
        for(int character = 0; character < 26; character++){
            amount = characterValues.size();
            characterValues.put((char)(capital + character), amount);
        }
        for(int character = 0; character < 10; character++){
            amount = characterValues.size();
            characterValues.put((char)(number + character), amount);
        }
        for(int character = 32; character != 127; character++){
            if(!characterValues.containsKey((char)character) && ((char)character) != '`' && ((char)character) != '~' && ((char)character) != '\\'){
                amount = characterValues.size();
                characterValues.put((char)(character), amount);
            }
        }

        for (char character : characterValues.keySet()) {
            byteCharacters.put(characterValues.get(character), character);
        }
    }

    public String getCipherId(){
        return cipherId;
    }

    public List<CipherSpinnerData> getCipherNames(){
        return cipherNamesAndIds;
    }

    //this enciphers the plain text based on the key and the cipher
    public String encipherText(String plainText, String key){
        StringBuilder cipherText = new StringBuilder();
        if(checkIfKeyIsValid(key)){
            switch (cipherId){
                case "1":
                    cipherText.append(plainText);
                    break;
                case "2":
                    int keyValue = Integer.parseInt(key);
                    for(char character : plainText.toCharArray()){
                        if(characterValues.containsKey(character)){
                            int characterValue = characterValues.get(character);
                            int alteredValue = characterValue + keyValue;
                            char newCharacter;
                            if(alteredValue >= 0){
                                newCharacter = byteCharacters.get(alteredValue % characterValues.size());
                            }
                            else{
                                int adjust = (Math.abs(alteredValue) / characterValues.size());
                                int newValue = alteredValue + characterValues.size() * adjust;
                                if(newValue < 0){
                                    newValue += characterValues.size();
                                }
                                newCharacter = byteCharacters.get(newValue);
                            }
                            cipherText.append(newCharacter);
                        }
                        else{
                            cipherText.append(character);
                        }
                    }
                    break;
                case "3":
                    int affineKeyValue = Integer.parseInt(key.split(":")[0]);
                    int transformationValue = Integer.parseInt(key.split(":")[1]);
                    int alphabetSize = characterValues.size();
                    for(char character : plainText.toCharArray()){
                        if(characterValues.containsKey(character)){
                            char newCharacter;
                            int characterValue = characterValues.get(character);
                            int alteredValue = (affineKeyValue * characterValue) + transformationValue;
                            if(alteredValue >= 0){
                                newCharacter = byteCharacters.get(alteredValue % alphabetSize);
                            }
                            else{
                                int adjust = (Math.abs(alteredValue) / alphabetSize);
                                int newValue = alteredValue + (alphabetSize * adjust);
                                if(newValue < 0){
                                    newValue += alphabetSize;
                                }
                                newCharacter = byteCharacters.get(newValue);
                            }
                            cipherText.append(newCharacter);
                        }else{
                            cipherText.append(character);
                        }
                    }
                    break;
            }
        }
        return cipherText.toString();
    }

    //this deciphers the cipher text based on the key and the cipher
    public String decipherText(String cipherText, String key){
        StringBuilder plainText = new StringBuilder();
        if(checkIfKeyIsValid(key)){
            switch (cipherId){
                case "1":
                    plainText.append(cipherText);
                    break;
                case "2":
                    int keyValue = Integer.parseInt(key);
                    for(char character : cipherText.toCharArray()){
                        if(characterValues.containsKey(character)){
                            int characterValue = characterValues.get(character);
                            int alteredValue = characterValue - keyValue;
                            char newCharacter;
                            if(alteredValue >= 0){
                                newCharacter = byteCharacters.get(alteredValue % characterValues.size());
                            }
                            else{
                                int adjust = (Math.abs(alteredValue) / characterValues.size());
                                int newValue = alteredValue + (characterValues.size() * adjust);
                                if(newValue < 0){
                                    newValue += characterValues.size();
                                }
                                newCharacter = byteCharacters.get(newValue);
                            }
                            plainText.append(newCharacter);
                        }
                        else{
                            plainText.append(character);
                        }
                    }
                    break;
                case "3":
                    int affineKeyValue = Integer.parseInt(key.split(":")[0]);
                    int transformationValue = Integer.parseInt(key.split(":")[1]);
                    int alphabetSize = characterValues.size();
                    int inverseAffineKeyValue = modularInverse(affineKeyValue, alphabetSize);

                    for(char character : cipherText.toCharArray()){
                        if(characterValues.containsKey(character)){
                            char newCharacter;
                            int characterValue = characterValues.get(character);
                            int alteredValue = inverseAffineKeyValue * (characterValue - transformationValue);
                            if(alteredValue >= 0){
                                newCharacter = byteCharacters.get(alteredValue % alphabetSize);
                            }
                            else{
                                int adjust = (Math.abs(alteredValue) / alphabetSize);
                                int newValue = alteredValue + (alphabetSize * adjust);
                                if(newValue < 0){
                                    newValue += alphabetSize;
                                }
                                newCharacter = byteCharacters.get(newValue);
                            }
                            plainText.append(newCharacter);
                        }else{
                            plainText.append(character);
                        }
                    }
                    break;
            }
        }
        return plainText.toString();
    }

    //this is to check if key follows the rules of the cipher
    public boolean checkIfKeyIsValid(String key){
        boolean isValidKey = false;
        if(cipherId != null){
            switch (cipherId){
                case "1":
                    isValidKey = true;
                    break;
                case "2":
                    try {
                        Integer.parseInt(key);
                        isValidKey = true;
                    } catch (NumberFormatException e) {
                        isValidKey = false;
                    }
                    break;
                case "3":
                    if(key.split(":").length == 2){
                        String affineNumberString = key.split(":")[0];
                        String transformationNumberString = key.split(":")[1];
                        try{
                            Integer.parseInt(transformationNumberString);
                            int affineNumberInt = Integer.parseInt(affineNumberString);
                            if(affineNumberInt > 0 && affineNumberInt < characterValues.size()){
                                isValidKey = isCoprime(affineNumberInt, characterValues.size());
                            }
                        } catch (NumberFormatException e){
                            isValidKey = false;
                        }
                    }
                    break;
            }
        }
        return isValidKey;
    }

    //finds the modular inverse of coprime numbers
    private int modularInverse(int number, int mod){
        int inverse;

        if(number >= 0){
           number %= mod;
        }
        else{
            int adjust = number / mod;
            number = number + (mod * adjust);
            if(number < 0){
                number += mod;
            }
        }

        for(inverse = 1; inverse < mod && (number * inverse) % mod != 1; inverse++){
        }

        return inverse;
    }

    //this is to find the greatest common divisor
    private int greatestCommonDivisor(int firstNumber, int secondNumber){
        if(firstNumber >= 0 && secondNumber >= 0){
            if(firstNumber < secondNumber){
                int tempNumber = firstNumber;
                firstNumber = secondNumber;
                secondNumber = tempNumber;
            }
            int remainder = firstNumber % secondNumber;

            if(remainder == 0){
                return secondNumber;
            }

            return greatestCommonDivisor(secondNumber, remainder);
        }
        else {
            return -1;
        }
    }

    //this is to tell if two numbers are coprime
    private boolean isCoprime(int firstNumber, int secondNumber){
        boolean isCoprime;
        isCoprime = greatestCommonDivisor(firstNumber, secondNumber) == 1;
        return isCoprime;
    }

    //this is to be implemented later when ciphers are dynamic
    public void setCurrentCipher(String cipherId){
        this.cipherId = cipherId;
    }

    private void createCipherRules(){

    }
}
