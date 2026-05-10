# Checklist opdracht Android IoT App

## Wat zit in dit project?

| Vereiste uit opdracht | In dit project |
|---|---|
| Android IoT App | Ja, Philips Hue / Hue Emulator controller |
| Kotlin | Ja |
| Android Studio project | Ja |
| REST API | Ja, via Retrofit |
| IoT apparaat | Philips Hue / Hue Emulator |
| HTTP permissie | Ja, `INTERNET` in AndroidManifest.xml |
| Cleartext HTTP | Ja, `android:usesCleartextTraffic="true"` |
| Niet-vluchtige opslag | Ja, via DataStore |
| IP-adres opslaan | Ja |
| Username/secret opslaan | Ja |
| Meerdere Activities | Ja, `MainActivity` en `LampDetailActivity` |
| Menu | Ja, via Compose TopAppBar met Instellingen, Reset en Vernieuwen |
| Voorbeeld menu XML | Aanwezig in `res/menu/main_menu.xml`, gebaseerd op het voorbeeldproject |
| Overzicht lampen | Ja, lijst met lampen in MainActivity |
| Lamp instellen | Ja, detail activity met aan/uit, helderheid en kleur |
| Simulatiemodus | Ja, de app blijft werken zonder echte Hue Emulator |
| Verslagtekst | Ja, `VERSLAG.md` |

## URLs Philips Hue / Hue Emulator

De app bouwt de URLs automatisch op basis van IP-adres en username:

```text
GET http://<ip-adres>/api/<username>/lights
PUT http://<ip-adres>/api/<username>/lights/<lampId>/state
```

Voorbeeld:

```text
GET http://192.168.1.50/api/QScScRGIUH581BZOxzAoTrW76rN38GfgXd9QIFyz/lights
PUT http://192.168.1.50/api/QScScRGIUH581BZOxzAoTrW76rN38GfgXd9QIFyz/lights/1/state
```

## Gebruikt voorbeeldproject

Het voorbeeldproject `MyApplicationExploreActivity` toont vooral:

- meerdere activities
- intent naar tweede activity
- menu via `res/menu/main_menu.xml`
- menu-items zoals Add en Remove

In deze IoT-app werd hetzelfde idee toegepast, maar aangepast aan Jetpack Compose:

- `MainActivity` = overzicht van lampen
- `LampDetailActivity` = lamp instellen
- menu bevat Instellingen, Reset connectie en Vernieuwen
- instellingen worden opgeslagen met DataStore

Omdat deze opdracht met Jetpack Compose werkt, is het zichtbare menu gemaakt met een Compose `TopAppBar` en `DropdownMenu`. Het XML-menu staat ook in het project als referentie naar het voorbeeld.
