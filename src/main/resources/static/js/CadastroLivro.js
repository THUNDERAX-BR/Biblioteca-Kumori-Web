$(document).ready(function () {

    $("#btnNavBuscarLivros").on("click", function () {
        location.href = "/buscarlivros";
    });

    $("#btnNavBuscarAutores").on("click", function () {
        location.href = "/buscarautores";
    });

    $("#btnNavGerenciar").on("click", function () {
        location.href = "/gerenciar";
    });

    $("#btnNavSair").on("click", function () {
        location.href = "/login/logout";
    });

    $("#btnOpenMenu").on("click", function () {
        $("#sideMenu").css("left", "0");
        $("#menuOverlay").show();
    });

    $("#menuOverlay").on("click", function () {
        $("#sideMenu").css("left", "-220px");
        $(this).hide();
    });

    $(window).on("resize", function () {
        if (window.innerWidth > 768) {
            $("#sideMenu").css("left", "0");
            $("#menuOverlay").hide();
        }
    });

    $("#btnSelecionarCapa").on("click", function () {
        $("#inputCapa").click();
    });

    $("#inputCapa").on("change", function () {
        if (this.files.length > 0) {
            $("#textoCapa").text(this.files[0].name);
            $("#alterarCapa").val("true");
        } else {
            $("#textoCapa").text("Sem alteração");
            $("#alterarCapa").val("false");
        }
    });

    $("#btnAdicionarCategoria").on("click", function () {

        const id = $("#selectCategoria").val();
        const texto = $("#selectCategoria option:selected").text();

        if (id === "") return;

        if ($("#cat_" + id).length > 0) return;

        const tag = $("<span>")
            .addClass("categoria-tag")
            .attr("id", "cat_" + id)
            .attr("data-id", id)
            .text(texto);

        $("#listaCategoriasSelecionadas").append(tag);
    });

    $("#listaCategoriasSelecionadas").on("click", ".categoria-tag", function () {
        $(this).remove();
    });

    $("#formLivro").on("submit", function () {

        if (!validarFormulario()) {
            alert("Preencha todos os campos obrigatórios corretamente.");
            return false;
        }

        let idsCategorias = [];
        $(".categoria-tag").each(function () {
            idsCategorias.push($(this).data("id"));
        });

        $("#categoriasIds").val(idsCategorias.join(","));

        return true;
    });

    function validarFormulario() {

        if ($("#tituloLivro").val().trim() === "") return false;
        if ($("#anoLivro").val().trim() === "") return false;
        if ($("#sinopseLivro").val().trim() === "") return false;

        if ($("#autorLivro").val() === "") return false;
        if ($("#editoraLivro").val() === "") return false;

        return true;
    }

});