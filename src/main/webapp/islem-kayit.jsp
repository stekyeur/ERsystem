<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hospital.model.Hemsire" %>
<%@ page import="com.hospital.model.IslemTuru" %>
<%@ page import="com.hospital.model.Birim" %>
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
                    <div class="col-md-6 mb-3">
                        <label for="tarih" class="form-label">Tarih</label>
                        <input type="date" class="form-control" id="tarih" name="tarih" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="hemsire_id" class="form-label">Hemşire</label>
                        <select class="form-select" id="hemsire_id" name="hemsire_id" required>
                            <option value="">Hemşire Seçiniz</option>
                            <%
                                List<Hemsire> hemsireler = (List<Hemsire>) request.getAttribute("hemsireler");
                                if (hemsireler != null) {
                                    for (Hemsire hemsire : hemsireler) {
                            %>
                            <option value="<%= hemsire.getId() %>">
                                <%= hemsire.getAdSoyad() %>
                                <% if (hemsire.getTecrube() != null && hemsire.getTecrube().getKategoriAdi() != null) { %>
                                (<%= hemsire.getTecrube().getKategoriAdi() %>)
                                <% } %>
                            </option>
                            <% } } %>
                        </select>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="islem_id" class="form-label">İşlem</label>
                        <select class="form-select" id="islem_id" name="islem_id" required onchange="loadBirimler()">
                            <option value="">İşlem Seçiniz</option>
                            <%
                                List<IslemTuru> islemTurleri = (List<IslemTuru>) request.getAttribute("islemTurleri");
                                if (islemTurleri != null) {
                                    for (IslemTuru islemTuru : islemTurleri) {
                            %>
                            <option value="<%= islemTuru.getId() %>" data-tip="<%= islemTuru.getIslemTipi() %>">
                                <%= islemTuru.getIslemAdi() %> (<%= islemTuru.getIslemTipi() %>)
                            </option>
                            <% } } %>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="birim_id" class="form-label">Birim</label>
                        <select class="form-select" id="birim_id" name="birim_id" required>
                            <option value="">Önce işlem seçiniz</option>
                        </select>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="sure_dakika" class="form-label">Süre (Dakika)</label>
                        <input type="number" class="form-control" id="sure_dakika" name="sure_dakika" min="1" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="islem_tipi" class="form-label">İşlem Tipi</label>
                        <select class="form-select" id="islem_tipi" name="islem_tipi" required>
                            <option value="">Seçiniz</option>
                            <option value="DIRECT">Direct</option>
                            <option value="INDIRECT">Indirect</option>
                        </select>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="kritiklik" class="form-label">Kritiklik</label>
                        <select class="form-select" id="kritiklik" name="kritiklik" required>
                            <option value="">Seçiniz</option>
                            <option value="CRITICAL">Kritik</option>
                            <option value="NON_CRITICAL">Kritik Değil</option>
                        </select>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="notlar" class="form-label">Notlar</label>
                    <textarea class="form-control" id="notlar" name="notlar" rows="3"></textarea>
                </div>

                <input type="hidden" name="kaydedilen_kullanici_id" value="<%= session.getAttribute("kullaniciId") %>">

                <button type="submit" class="btn btn-primary">Kaydet</button>
                <a href="dashboard.jsp" class="btn btn-secondary">İptal</a>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.getElementById('tarih').valueAsDate = new Date();

    function loadBirimler() {
        const islemSelect = document.getElementById('islem_id');
        const birimSelect = document.getElementById('birim_id');
        const islemTipiSelect = document.getElementById('islem_tipi');
        const selectedIslemId = islemSelect.value;
        const selectedIslemTip = islemSelect.options[islemSelect.selectedIndex]?.getAttribute('data-tip');

        if (selectedIslemTip) {
            islemTipiSelect.value = selectedIslemTip;
        }

        if (selectedIslemId) {
            fetch('islem-kayit?action=getBirimlerByIslem&islemId=' + selectedIslemId)
                .then(response => response.json())
                .then(birimler => {
                    birimSelect.innerHTML = '<option value="">Birim Seçiniz</option>';
                    birimler.forEach(birim => {
                        birimSelect.innerHTML += `<option value="${birim.id}">${birim.birimAdi}</option>`;
                    });
                })
                .catch(error => {
                    console.error('Birimler yüklenirken hata oluştu:', error);
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
