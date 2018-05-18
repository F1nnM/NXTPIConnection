var source = new EventSource("data.php");
source.onmessage = function (event) {

    log(event.data);
    console.log(getRecordState());

    coords = (/\d{1,3}-\d{1,3}/im).exec(event.data);
    x = (/\d{1,3}/im).exec(coords)[0];
    y = (/-\d{1,3}/im).exec(coords)[0];
    y = y.substring(1, y.length);
    obj1 = new Object;
    obj2 = new Object;
    obj1.x = +x;
    obj2.y = +x;
    obj1.y = +y;
    obj2.x = +y;
    drawImg(canvas,[obj1, obj2]);
};

function log(text) {
    ta = document.getElementById("displaytext");
    scroll = (ta.scrollTop > ta.scrollHeight - ta.offsetHeight);
    ta.value += text + "\n";
    if (scroll) ta.scrollTop = ta.scrollHeight;
}

function getRecordState() {
    state1 = (document.getElementById("btn1").getAttribute("state")=="stop");
    state2 = (document.getElementById("btn2").getAttribute("state")=="pause");
    if (state1&&state2) return "recording";
    if (state1&&(!state2)) return "paused";
    return "stopped";
}

var canvas = document.getElementById("canvas2D");
var ctx = canvas.getContext("2d");
ctx.canvas.width = canvas.offsetWidth;
ctx.canvas.height = canvas.offsetHeight;

var positions = [];
var map = [];
var highest_x = 0;
var highest_y = 0;

function drawImg(canvas, new_positions) {
    var ctx = canvas.getContext("2d");
    ctx.fillStyle = "#ff3300";
    positions.push.apply(positions, new_positions);

    new_positions.forEach(function (pos) {
        if (pos.y > highest_y) {
            highest_y = pos.y + 1;
        }

        if (pos.x > highest_x) {
            highest_x = pos.x + 1;
        }
    });

    createMap();

    var scaled_positions = scale(canvas);

    for (var x = 0; x < highest_x; x++) {
        for (var y = 0; y < map[x].length; y++) {
            if (map[x][y]) {
                ctx.fillRect(x, y, 1, 1);
            }
        }
    }
}

function scale(ctx) {
    if (highest_x > canvas.width || highest_y > canvas.height) {
        var scaled_positions = [];
        var x_percentage = highest_x / ctx.canvas.width;
        var y_percentage = highest_y / ctx.canvas.height;
        var delta;

        if (x_percentage > y_percentage) {
            delta = Math.floor(x_percentage);
        } else {
            delta = Math.floor(y_percentage);
        }

        for (x = 0; x < map.length; x = x + delta) {
            var y_positions = [];
            for (var y = 0; y < map[x].length; y = y + delta) {
                y_positions.push(map[x][y]);
            }
            scaled_positions.push(y_positions);
        }

        return scaled_positions;
    }
    return map;
}

function createMap() {
    map = [];
    for (var x = 0; x < highest_x; x++) {
        var y_positions = [];
        for (var y = 0; y < highest_y; y++) {
            y_positions[y] = false;
        }
        map[x] = y_positions.slice();
    }

    positions.forEach(function (pos) {
        map[pos.x][pos.y] = true;
    });
}