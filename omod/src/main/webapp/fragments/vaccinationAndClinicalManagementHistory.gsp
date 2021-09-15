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
            <th align="left">Vaccine</th>
            <th align="left">Dose</th>
            <th align="left">Date</th>
            <th align="left">Verified</th>
        </tr>
        <% firstAndSecondDoseList.each { %>
        <tr>
            <td>${it.vaccineType}</td>
            <td>${it.vaccinationDose}</td>
            <td>${it.vaccinationDate}</td>
            <td>${it.vaccinationVerified}</td>
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
            <th align="left">Vaccine</th>
            <th align="left">Dose</th>
            <th align="left">Date</th>
            <th align="left">Verified</th>
        </tr>
        <% boosterDoseList.each { %>
        <tr>
            <td>${it.vaccineType}</td>
            <td>${it.vaccinationDose}</td>
            <td>${it.vaccinationDate}</td>
            <td>${it.vaccinationVerified}</td>
        </tr>
        <% } %>
    </table>
    <% } else {%>
        <div>No history found</div>

    <% } %>
</fieldset>

    <br/>
    <fieldset>
    <legend>COVID-19 Clinical Management History</legend>
    <table class="simple-table">

            <tr>
                <th align="left">Date reported</th>
                <th align="left">Hospital admission</th>
                <th align="left">Admission units</th>
                <th align="left">Clinical Management</th>
            </tr>
            </table>
</fieldset>


</div>
