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

    $("#btnOpenMenu").on("click", () => {
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

    $("#btnSelecionarFoto").on("click", () => $("#inputFoto").click());
    $("#inputFoto").on("change", function () {
        if (this.files.length > 0) {
            $("#textoFoto").text(this.files[0].name);
            $("#alterarFoto").val("true");
        } else {
            $("#textoFoto").text("Sem alteração");
            $("#alterarFoto").val("false");
        }
    });

    $("#formAutor").on("submit", function () {
        if (!validarFormulario()) {
            alert("Preencha todos os campos obrigatórios corretamente.");
            return false;
        }
        return true;
    });

    function validarFormulario() {
        if ($("#nomeAutor").val().trim() === "") return false;
        if ($("#movimentoAutor").val() === "") return false;
        if ($("#dataNascimento").val().trim() === "") return false;
        if ($("#biografiaAutor").val().trim() === "") return false;

        const nascimento = $("#dataNascimento").val();
        const falecimento = $("#dataFalecimento").val();
        if (falecimento && falecimento <= nascimento) {
            alert("A data de falecimento deve ser posterior à data de nascimento.");
            return false;
        }
        return true;
    }

});
