<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hospital.model.Islem" %>
<%@ page import="com.hospital.model.Hemsire" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>İşlem Kayıt - Acil Servis</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container mt-4">
    <h2>İşlem Kayıt</h2>

    <% if (request.getAttribute("basari") != null) { %>
    <div class="alert alert-success">
        <%= request.getAttribute("basari") %>
    </div>
    <% } %>

    <% if (request.getAttribute("hata") != null) { %>
    <div class="alert alert-danger">
        <%= request.getAttribute("hata") %>
    </div>
    <% } %>

    <div class="card">
        <div class="card-body">
            <form action="islem-kayit" method="post">
                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="tarih" class="form-label">Tarih</label>
                            <input type="date" class="form-control" id="tarih" name="tarih" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="hemsire" class="form-label">Hemşire</label>
                            <select class="form-select" id="hemsire" name="hemsire" required>
                                <option value="">Hemşire Seçiniz</option>
                                <%
                                    List<Hemsire> hemsireler = (List<Hemsire>) request.getAttribute("hemsireler");
                                    if (hemsireler != null) {
                                        for (Hemsire hemsire : hemsireler) {
                                %>
                                <option value="<%= hemsire.getHemsireId() %>">
                                    <%= hemsire.getAdSoyad() %>
                                    (<%= hemsire.getTecrubeSeviyesi() != null ? hemsire.getTecrubeSeviyesi() : "" %>)
                                </option>
                                <% } } %>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="islem" class="form-label">İşlem</label>
                            <select class="form-select" id="islem" name="islem" required onchange="loadBirimler()">
                                <option value="">İşlem Seçiniz</option>
                                <%
                                    List<Islem> islemler = (List<Islem>) request.getAttribute("islemler");
                                    if (islemler != null) {
                                        for (Islem islem : islemler) {
                                %>
                                <option value="<%= islem.getId() %>" data-tip="<%= islem.isDirect() ? "DIRECT" : "INDIRECT" %>">
                                    <%= islem.getIslemAdi() %> (<%= islem.isDirect() ? "DIRECT" : "INDIRECT" %>)
                                </option>
                                <% } } %>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="birim" class="form-label">Birim</label>
                            <select class="form-select" id="birim" name="birim" required>
                                <option value="">Önce işlem seçiniz</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-4">
                        <div class="mb-3">
                            <label for="sure" class="form-label">Süre (Dakika)</label>
                            <input type="number" class="form-control" id="sure" name="sure" min="1" required>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="mb-3">
                            <label for="islemTipi" class="form-label">İşlem Tipi</label>
                            <select class="form-select" id="islemTipi" name="islemTipi" required>
                                <option value="">Seçiniz</option>
                                <option value="DIRECT">Direct</option>
                                <option value="INDIRECT">Indirect</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="mb-3">
                            <label for="kritiklik" class="form-label">Kritiklik</label>
                            <select class="form-select" id="kritiklik" name="kritiklik" required>
                                <option value="">Seçiniz</option>
                                <option value="CRITICAL">Kritik</option>
                                <option value="NON_CRITICAL">Kritik Değil</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="notlar" class="form-label">Notlar</label>
                    <textarea class="form-control" id="notlar" name="notlar" rows="3"></textarea>
                </div>

                <button type="submit" class="btn btn-primary">Kaydet</button>
                <a href="dashboard" class="btn btn-secondary">İptal</a>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Bugünün tarihini varsayılan olarak set et
    document.getElementById('tarih').valueAsDate = new Date();

    function loadBirimler() {
        const islemSelect = document.getElementById('islem');
        const birimSelect = document.getElementById('birim');
        const islemTipiSelect = document.getElementById('islemTipi');

        const selectedOption = islemSelect.options[islemSelect.selectedIndex];
        const islemTipi = selectedOption.getAttribute('data-tip');

        // İşlem tipini otomatik set et
        if (islemTipi) {
            islemTipiSelect.value = islemTipi;
        }

        if (islemSelect.value) {
            fetch('islem-kayit?action=getBirimlerByIslem&islemId=' + islemSelect.value)
                .then(response => response.json())
                .then(data => {
                    birimSelect.innerHTML = '<option value="">Birim Seçiniz</option>';
                    data.forEach(birim => {
                        birimSelect.innerHTML += `<option value="${birim.id}">${birim.birimAdi}</option>`;
                    });
                })
                .catch(error => {
                    console.error('Error:', error);
                    birimSelect.innerHTML = '<option value="">Hata oluştu</option>';
                });
        } else {
            birimSelect.innerHTML = '<option value="">Önce işlem seçiniz</option>';
            islemTipiSelect.value = '';
        }
    }
</script>
</body>
</html>