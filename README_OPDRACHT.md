# IoTApp - Android IoT App met Jetpack Compose

Deze versie bevat de onderdelen uit de opdracht:

- Android app in Kotlin
- Jetpack Compose UI
- meerdere schermen/activities:
  - `MainActivity`: overzicht van lampen
  - `LampDetailActivity`: kleur en helderheid van één lamp instellen
- menu met:
  - Instellingen
  - Reset connectie
  - Vernieuwen
- REST API structuur met Retrofit
- Philips Hue / Hue Emulator URLs
- niet-vluchtige opslag via DataStore:
  - IP-adres
  - username / secret
- HTTP permissie in `AndroidManifest.xml`
- `android:usesCleartextTraffic="true"` voor HTTP-requests naar de Hue Emulator
- simulatiemodus zodat de app ook werkt zonder echte Hue Emulator

## Gebruikte REST URLs

De app gebruikt deze Philips Hue structuur:

```text
GET http://<ip-adres>/api/<username>/lights
PUT http://<ip-adres>/api/<username>/lights/<lamp-id>/state
```

Voorbeeld:

```text
GET http://192.168.1.50/api/QScScRGIUH581BZOxzAoTrW76rN38GfgXd9QIFyz/lights
PUT http://192.168.1.50/api/QScScRGIUH581BZOxzAoTrW76rN38GfgXd9QIFyz/lights/1/state
```

## Hue Emulator gebruiken

Start de emulator met:

```text
java -jar HueEmulator-v0.8.jar
```

Gebruik daarna het IP-adres en de username/secret in de app via:

```text
Menu > Instellingen
```

Als er geen correcte connectie is, blijft de app werken in simulatiemodus. Dat is handig om screenshots te maken en de UI te tonen.

## Waar zit de code?

```text
app/src/main/java/com/example/iotapp/
├── MainActivity.kt
├── LampDetailActivity.kt
├── data
│   ├── api
│   │   ├── HueApi.kt
│   │   └── RetrofitInstance.kt
│   ├── datastore
│   │   └── SettingsDataStore.kt
│   ├── model
│   │   ├── HueModels.kt
│   │   └── LampState.kt
│   └── repository
│       └── LampRepository.kt
├── ui
│   └── screens
│       └── HomeScreen.kt
└── viewmodel
    └── LampViewModel.kt
```

## Aanpassing op basis van voorbeeldproject

Er werd rekening gehouden met het aangeleverde voorbeeldproject `MyApplicationExploreActivity`.
Dat voorbeeld toont hoe je met meerdere activities en een menu werkt.
In deze app is dat vertaald naar:

- `MainActivity`: overzicht van alle Hue-lampen
- `LampDetailActivity`: detailpagina om één lamp te bedienen
- menu in de TopAppBar: Instellingen, Reset connectie en Vernieuwen
- intent extra: het lamp-id wordt doorgestuurd naar de detail activity

De opdracht gebruikt Jetpack Compose, daarom werd de interface niet in XML-layouts gemaakt maar in Compose.
