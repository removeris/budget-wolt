# Maisto rezervavimo ir valdymo sistema

## Projekto paskirtis
Sistema skirta maisto užsakymų valdymui tarp klientų, restoranų savininkų, vairuotojų ir administratorių. Tikslas – užtikrinti sklandų užsakymų priėmimo, paruošimo, pristatymo ir administravimo procesą.

## Pagrindiniai funkcionalumai

### Bendri reikalavimai
- Sistema turi autentifikacijos mechanizmą – prisijungimas su vartotojo vardu ir slaptažodžiu.
- Slaptažodžiai duomenų bazėje saugomi “hashed” formatu.
- Skirtingi vartotojų tipai turi ribotą prieigą prie duomenų ir funkcijų pagal rolę.
- Duomenys saugomi duomenų bazėje, prieiga vykdoma per ORM ar SQL užklausas.

### Vartotojų tipai
#### 1. Klientas
- Registruojasi ir prisijungia prie sistemos.
- Matyti restoranų sąrašą ir jų meniu.
- Sudaro užsakymo krepšelį iš pasirinkto restorano.
- Atlieka užsakymą ir stebi jo būseną.
- Gali bendrauti su restoranu ar vairuotoju prie konkretaus užsakymo.
- Gali palikti atsiliepimus ir įvertinimus restoranui ar vairuotojui.
- Dalyvauja lojalumo programoje (bonus taškai už užsakymus).

#### 2. Restorano savininkas
- Prisijungia prie sistemos per “Desktop” programą.
- Kuria, redaguoja ir trina savo restorano meniu patiekalus.
- Matyti gautus užsakymus ir keisti jų būseną (priimtas, ruošiamas, paruoštas).
- Gali bendrauti su klientu ir vairuotoju prie konkretaus užsakymo.
- Gali palikti atsiliepimus apie klientus.

#### 3. Vairuotojas
- Prisijungia prie sistemos per mobilų įrenginį.
- Matyti užsakymus, kuriuos galima paimti.
- Paima užsakymą, pristato klientui, atnaujina užsakymo būseną.
- Gali bendrauti su klientu ar restoranu prie konkretaus užsakymo.
- Palieka atsiliepimus apie klientus.

#### 4. Administratorius
- Prisijungia prie sistemos per “Desktop” programą.
- Gali matyti ir redaguoti visus sistemos duomenis (vartotojus, restoranus, užsakymus, atsiliepimus).
- Sprendžia konfliktines situacijas tarp vartotojų.
- Gali blokuoti vartotojus ar šalinti netinkamą turinį.

---

## Papildomi funkcionalumai
- Filtravimas pagal įvairius požymius (pvz. kaina, įvertinimas, atstumas, pristatymo laikas, virtuvės tipas).
- Dinaminis kainų modifikavimas pagal laiką (piko metu – brangiau, ne piko – pigiau).
- Bonus taškų (lojalumo) sistema ištikimiems klientams.
- Susirašinėjimo sistema tarp užsakymo dalyvių (klientas–restoranas–vairuotojas).
- Atsiliepimų ir įvertinimų sistema (restoranams, vairuotojams ir klientams).

---

## Platformos
- **Desktop programa** – restoranų savininkams ir administratoriams.
- **Mobilioji programėlė** – klientams ir vairuotojams.
