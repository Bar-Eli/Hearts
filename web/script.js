
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */





/* global mag */

$(document).ready(function ()
{
    var webSocket;

    var suitCount = new Object();
    suitCount.s = 0;
    suitCount.h = 0;
    suitCount.d = 0;
    suitCount.c = 0;

    var roundSuit = undefined;

    //var debug = false;

    var is2c = false;


    var originals = [];

    function init() {
        var playersHand = ["#playerHand", "#westHand", "#northHand", "#eastHand"];
        for (var i = 0; i < playersHand.length; i++)
            originals[i] = $(playersHand[i]).clone();
        //$(playersHand[i]).data('original-state', $(playersHand[i]).html());



    }

    function reset() {
        suitCount.s = 0;
        suitCount.h = 0;
        suitCount.d = 0;
        suitCount.c = 0;
        is2c = false;
        clearBoard();
        var playersHand = ["#playerHand", "#westHand", "#northHand", "#eastHand"];
        for (var i = 0; i < playersHand.length; i++)
            $(playersHand[i]).replaceWith(originals[i].clone());
        //$(playersHand[i]).html($(playersHand[i]).data('original-state'));


        roundSuit = undefined;

        //debug = true;


    }

    function changeCard(cardVal, card) {
        // Create suits 'ditionary'.
        var suits = new Object();
        suits.s = "spades";
        suits.h = "hearts";
        suits.d = "diams";
        suits.c = "clubs";

        var suit = suits[cardVal.charAt(1)]; // get card's suit from cardVal string and it's full name from the suits dictionary.
        var rank = cardVal.charAt(0).toLowerCase(); // get card's rank from cardVal string.
        if (rank === 't') // only case rank on css is different than the one given from server.
            rank = "10";

        // Add card to board:
        // Remove specific card css class in case it already has one.
        $(card).attr("class", "card");
        // Add css class (inner card symbols).
        card.classList.add("rank-" + rank);
        card.classList.add(suit);
        // Add to inner HTMLs (card's value on corner).
        card.firstElementChild.innerHTML = rank.toUpperCase();
        card.lastElementChild.innerHTML = '&' + suit + ';';
    }

    function dealCards(hand) {

        // Create suits 'ditionary'.
        var suits = new Object();
        suits.s = "spades";
        suits.h = "hearts";
        suits.d = "diams";
        suits.c = "clubs";

        var pCards = $("#playerHand").children(); // list of player's cards on web page ('li' elements).
        var card; // 'a' element for evry card in hand

        // Assign All cards in order
        for (var i = 0; i < pCards.length; i++)
        {
            card = pCards[i].firstElementChild;
            var cardVal = hand.slice(2 * i, 2 * i + 2);

            if (cardVal === "2c")
                is2c = true;

            suitCount[cardVal.charAt(1)] += 1;
            changeCard(cardVal, card);

        }
    }

    // Detect card clicked by user.
    var cardClicked; // Clicked card value  in 2 chars string formation.
    var CardLI; // Card li element.
    var playBtn = $("#playBtn");

    //$("li").click(function () {
    //$("li").on("click", function () {
    $(document).on("click", "li", function () {
        var suits = new Object();
        suits["♠"] = 's';
        suits["♥"] = 'h';
        suits["♦"] = 'd';
        suits["♣"] = 'c';


        rank = this.firstElementChild.firstElementChild.innerHTML;
        if (rank === "10")
            rank = "t";
        suit = suits[this.firstElementChild.lastElementChild.innerHTML];
        cardClicked = rank + suit;
        CardLI = this;



        if (roundSuit === undefined || suit === roundSuit || suitCount[roundSuit] === 0)
            playBtn.attr("disabled", false);
        else
            playBtn.attr("disabled", true);

        if (is2c === true && cardClicked !== "2c")
            playBtn.attr("disabled", true);


        //if (debug === true)
        //  playBtn.attr("disabled", true);


    });

    // Play button click
    playBtn.click(function () {

        // Make sure a card was selected
        if (CardLI === undefined)
            return;

        suitCount[cardClicked.charAt(1)]--;

        // Change card on board
        var playerCard = $("#playerCard");
        changeCard(cardClicked, playerCard[0]);
        sendMessage(cardClicked);

        // Remove from player hand
        CardLI.remove();


        CardLI = undefined; // force playing only when a card is selected.

        if (is2c === true && cardClicked === "2c")
            is2c = false


    });

    function playCard(player, cardPlayed)
    {
        var handID = "#" + player + "Hand";
        var cardID = "#" + player + "Card";

        if (player !== "south")
            $(handID)[0].firstElementChild.remove();
        changeCard(cardPlayed, $(cardID)[0]);

    }

    function clearBoard()
    {
        var players = ["#playerCard", "#westCard", "#northCard", "#eastCard"];
        for (var i = 0; i < players.length; i++)
        {
            c = $(players[i])[0];
            $(c).attr("class", "card");
            c.firstElementChild.innerHTML = '';
            c.lastElementChild.innerHTML = '';
        }
        roundSuit = undefined;
        playBtn.attr("disabled", true);


    }

    // Modal (result alert) functions)

    var span = document.getElementsByClassName("close")[0];
    var modal = document.getElementById("resultsModal");
    // When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
    };

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    };

    var resTag = document.getElementById("resString");
    var resTitleTag = document.getElementById("ModalTitle");
    function alertResulr(msg)
    {
        resTitleTag.innerHTML = msg["resTitle"];
        resTag.innerHTML = msg["res"];
        modal.style.display = "block";

    }




    function writeResponse(data)
    {
//        alert(data);
    }

    // Web Socket funcions.
    function createConnection()
    {

        // open web socket
        // bind websocket triggered events to functions:
        // onOpen, onMessage, onClose
        // Senddata is triggered by user
        // Ensures only one connection is open at a time
        if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
            writeResponse("WebSocket is already opened.");
            return;

        }


        // Establish web socket 
        var addr = "ws://" + document.domain + ":" + location.port + "/HeartsProject/HeartsProject";
        webSocket = new WebSocket(addr);

        init();

        // Bind functions to the listeners for the websocket.
        webSocket.onopen = function (event) {
            // For reasons I can't determine, onopen gets called twice
            // and the first time event.data is undefined.
            // Leave a comment if you know the answer.

            if (event.data === undefined)
                return;

            writeResponse(event.data);
        };

        webSocket.onmessage = function (event) {

            var msg = JSON.parse(event.data); // Read message from server.

            if (msg["type"] === "init") // On Open messa ge -- deal cards.
                dealCards(msg["hand"]);
            if (msg["type"] === "play")
                playCard(msg["player"], msg["card"]);
            if (msg["type"] === "clear")
                clearBoard();
            if (msg["type"] === "suit")
                roundSuit = msg["val"];
            if (msg["type"] === "res")
                alertResulr(msg);
            //alert(msg["res"]);
            if (msg["type"] === "reset")
                reset();


        };

        webSocket.onclose = function (event) {
            writeResponse("Connection closed");
        };



    }

    function sendMessage(data)
    {
        // read data from user and send to server
        /*  
         var obj = {"name": name,"data":data, "to": "everyone"};
         var objToSend = JSON.stringify(obj);
         webSocket.send(objToSend);
         */
        //webSocket.send(name + ":" + data);
        webSocket.send(data);
    }

    createConnection();

});




