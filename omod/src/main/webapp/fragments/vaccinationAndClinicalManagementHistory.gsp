<%
    ui.decorateWith("kenyaui", "panel", [ heading: "COVID-19 Vaccination and Clinical Management History" ])
%>
<style>
.simple-table {
    border: solid 1px #DDEEEE;
    border-collapse: collapse;
    border-spacing: 0;
    font: normal 13px Arial, sans-serif;
}
.simple-table thead th {
    background-color: #DDEFEF;
    border: solid 1px #DDEEEE;
    color: #336B6B;
    padding: 10px;
    text-align: left;
    text-shadow: 1px 1px 1px #fff;
}
.simple-table td {
    border: solid 1px #DDEEEE;
    color: #333;
    padding: 5px;
    text-shadow: 1px 1px 1px #fff;
}
</style>


<div>

    <fieldset>
    <legend>Vaccination History</legend>
    <%if (firstAndSecondDoseList) { %>
    <table class="simple-table">

        <tr>
            <th width="25%" align="left">Date</th>
            <th width="25%" align="left">Vaccine</th>
            <th width="25%" align="left">Dose</th>
            <th width="25%" align="left">Verified</th>
        </tr>
        <% firstAndSecondDoseList.each { %>
        <tr>
            <td width="25%">${it.vaccinationDate}</td>
            <td width="25%">${it.vaccineType}</td>
            <td width="25%">${it.vaccinationDose}</td>
            <td width="25%">${it.vaccinationVerified ?: it.vaccinationVerified}</td>
        </tr>
        <% } %>
    </table>
    <% } else {%>
        <div>No history found</div>

    <% } %>
</fieldset>

<br/>
<fieldset>
    <legend>Vaccination History (Booster)</legend>
    <%if (boosterDoseList) { %>
    <table class="simple-table">

        <tr>
            <th width="25%" align="left">Date</th>
            <th width="25%" align="left">Vaccine</th>
            <th width="25%" align="left">Sequence</th>
            <th width="25%" align="left">Verified</th>
        </tr>
        <% boosterDoseList.each { %>
        <tr>
            <td width="25%">${it.vaccinationDate}</td>
            <td width="25%">${it.vaccineType}</td>
            <td width="25%">${it.vaccinationDose}</td>
            <td width="25%">${it.vaccinationVerified ?: it.vaccinationVerified}</td>
        </tr>
        <% } %>
    </table>
    <% } else {%>
        <div>No history found</div>

    <% } %>
</fieldset>

    <br/>
<fieldset>
    <legend>COVID-19 Diagnosis and Clinical Management History</legend>
    <%if (diagnosisAndManagementList) { %>
        <table class="simple-table">
            <tr>
                <th align="left">Diagnosis Date</th>
                <th align="left">Presentation</th>
                <th align="left">Hospital Admission</th>
                <th align="left">Admission Units</th>
                <th align="left">Supplemental Oxygen</th>
                <th align="left">Ventilator</th>
            </tr>
            <% diagnosisAndManagementList.each { %>
            <tr>
                <td>${it.dateTested}</td>
                <td>${it.covidPresentation}</td>
                <td>${it.hospitalAdmission}</td>
                <td>${it.admissionUnits}</td>
                <td>${it.supplementalOxygen}</td>
                <td>${it.ventilator}</td>
            </tr>
            <% } %>
        </table>
        <% } else {%>
            <div>No history found</div>

        <% } %>
</fieldset>


</div>
