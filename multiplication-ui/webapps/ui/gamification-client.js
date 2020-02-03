var serverUrl = "http://localhost:8000/api";
function updateLeaderBoard() {
    $.ajax({
        url: serverUrl + "/leaders"
    }).then(function (data) {
        $('#leaderboard-body').empty();
        data.forEach(function(row) {
            $('#leaderboard-body').append('<tr><td>' + row.userId + '</td><td>' + row.totalScore + '</td></tr>');
        });
    });
}

function updateStats(userId) {
    $.ajax({
        url: serverUrl + "/stats?userId=" + userId,
        success: function(data) {
            $('#stats-div').show();
            $('#stats-user-id').empty().append(userId);
            $('#stats-score').empty().append(data.score);
            $('#stats-badges').empty().append(data.badges.join());
        },
        error: function(data) {
            $('#stats-div').show();
            $('#stats-user-id').empty().append(userId);
            $('#stats-score').empty().append(0);
            $('#stats-badges').empty();
        }
    });
}

$(document).ready(function () {
    updateLeaderBoard();
    $("#refresh-leaderboard").click(function( event ) {
        updateLeaderBoard();
    });
});