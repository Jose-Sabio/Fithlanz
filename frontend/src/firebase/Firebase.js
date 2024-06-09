import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";

const apiKey = import.meta.env.VITE_FIREBASE_APIKEY
const authDomain = import.meta.env.VITE_FIREBASE_AUTHDOMAIN
const projectId = import.meta.env.VITE_FIREBASE_PROJECTID
const storageBucket = import.meta.env.VITE_FIREBASE_STORAGEBUCKET
const messagingSenderId = import.meta.env.VITE_FIREBASE_MESSAGINGSENDERID
const appId = import.meta.env.VITE_FIREBASE_APPID


const firebaseConfig = {
    apiKey,
    authDomain,
    projectId,
    storageBucket,
    messagingSenderId,
    appId,
};

const firebaseApp = initializeApp(firebaseConfig);

export const db = getFirestore(firebaseApp);
