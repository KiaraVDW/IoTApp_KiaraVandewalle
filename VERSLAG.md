# Verslag Android IoT App

## Inleiding

Voor deze opdracht werd een Android IoT-app ontwikkeld met Kotlin en Jetpack Compose. De app is bedoeld om een IoT-apparaat aan te sturen. In dit project werd gekozen voor een Philips Hue-lamp of Philips Hue Emulator. De app kan ook zonder echte emulator getoond worden dankzij een simulatiemodus.

## Functionaliteiten

De app bevat een overzichtsscherm met verschillende lampen. Per lamp wordt de naam, status, helderheid en kleur weergegeven. Vanuit dit overzicht kan de gebruiker een lamp aan- of uitzetten en doorklikken naar een detailscherm.

In het detailscherm kan de gebruiker één specifieke lamp instellen. De gebruiker kan de lamp aan- of uitzetten, de helderheid aanpassen met een slider en de kleur instellen op rood, groen, blauw of wit.

Er is ook een menu voorzien. Via dit menu kan de gebruiker de instellingen openen, de connectie resetten of de lampstatus opnieuw laden. In de instellingen kan het IP-adres van de Hue bridge of Hue Emulator en de username/secret bewaard worden.

## REST API

De communicatie met het IoT-apparaat is voorbereid via Retrofit. De app gebruikt de REST API-structuur van Philips Hue:

```text
GET /api/<username>/lights
PUT /api/<username>/lights/<lamp-id>/state
```

De GET-request wordt gebruikt om de huidige status van de lampen op te halen. De PUT-request wordt gebruikt om de status, helderheid of kleur van een lamp aan te passen.

## Niet-vluchtige opslag

Het IP-adres en de username/secret worden opgeslagen met DataStore. Daardoor blijven deze gegevens bewaard wanneer de app wordt afgesloten en opnieuw geopend.

## Architectuur

De app gebruikt een duidelijke structuur:

- UI: Compose-schermen in `ui/screens`
- ViewModel: status en acties in `LampViewModel`
- Repository: communicatie tussen ViewModel en API
- API-laag: Retrofit-interface in `HueApi`
- DataStore: opslag van connectiegegevens

Deze structuur zorgt ervoor dat de UI gescheiden blijft van de logica en netwerkcommunicatie.

## Simulatiemodus

Als er geen IP-adres of username is ingevuld, of als de Hue Emulator niet bereikbaar is, toont de app simulatiegegevens. Zo blijft de app bruikbaar om de UI en werking te demonstreren. Wanneer de connectiegegevens correct zijn, probeert de app de echte REST API te gebruiken.

## Besluit

Tijdens deze opdracht werd een Android IoT-app opgebouwd met Kotlin, Jetpack Compose, DataStore en Retrofit. De app bevat meerdere schermen, een menu, niet-vluchtige opslag en een REST API-koppeling volgens de structuur van Philips Hue. De app is voorbereid om met een echte Philips Hue bridge of Hue Emulator te werken.
