SELECT Sitzungsdaten.SitzungsdatenID, Sitzungsdaten.Datum, Sitzungsdaten.Ort, 
Sitzungsdaten.Protokollant, Sitzungsdaten.Teilnehmer, Sitzungsart.Name, Kategorie.Name, 
Protokollelement.ToDoID, Protokollelement.KategorieID, Protokollelement.InstitutionsID, 
Institution.Name, Protokollelement.Thema, Protokollelement.Inhalt, Protokollelement.StatusID, 
Status.Name, Sitzungsdaten.Tagesordnung, Bereich.Name
FROM Bereich INNER JOIN (Status INNER JOIN (Institution INNER JOIN 
((Sitzungsart INNER JOIN Sitzungsdaten ON Sitzungsart.SitzungsartID = Sitzungsdaten.SitzungsartID) 
INNER JOIN (Kategorie INNER JOIN Protokollelement ON Kategorie.KategorieID = Protokollelement.KategorieID) 
ON Sitzungsdaten.SitzungsdatenID = Protokollelement.SitzungsID) ON Institution.InstitutionID = Protokollelement.InstitutionsID) 
ON Status.StatusID = Protokollelement.StatusID) ON Bereich.BereichID = Protokollelement.BereichID
WHERE Sitzungsdaten.SitzungsdatenID = 2;