package com.neves_eduardo.data_analysis_challenge;

public class Main {
    public static void main(String[] args) {
        DirectoryObserver directoryObserver = new DirectoryObserver();
        directoryObserver.appBoot();
        while(true){directoryObserver.appCheckDirectory();}

    }

}



