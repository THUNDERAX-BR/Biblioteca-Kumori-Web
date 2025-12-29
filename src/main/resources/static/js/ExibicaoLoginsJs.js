//Esse também e foi depois que eu corrigi os bugs :(
$(document).ready(function () {
    
    const usuarioLogadoId = Number($(".user-panel").data("id"));

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

    $("#btnCadastroLogin").on("click", function () {
        location.href = "/login/cadastro";
    });

    $(".btnExcluirLogin").on("click", function () {
        const id = Number($(this).data("id"));

        if (id === usuarioLogadoId) {
            alert("Você não pode excluir o login atualmente em uso.");
            return;
        }
        
        if (confirm("Deseja realmente excluir este login?")) {
            const form = $("<form>", {
                method: "POST",
                action: "/login/excluir/" + id
            });
            
            $(document.body).append(form);
            form.submit();
        }
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

    $(".btnBuscarGlobal").on("click", function () {
        const busca = $("#inputBuscaLogin").val();
        location.href = "/login/exibicao?busca=" + encodeURIComponent(busca);
    });

});