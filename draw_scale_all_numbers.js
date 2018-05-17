var positions = [];
var map = [];
var highest_x;
var highest_y;

function drawImg(canvas, new_positions){
    var ctx = canvas.getContext("2d");
    ctx.fillStyle = "#000000";
    positions.push.apply(positions, new_positions);

    if(positions.length > highest_x){
        highest_x = positions.length;
    }

    new_positions.forEach(function(y_pos) {
        if(y_pos.length > highest_y){
            highest_y = y_pos.length;
        }
    });

    createMap();

    var scaled_positions = scale(canvas);

    for(var x = 0; x < scaled_positions.length; x++){
        for (var y; y < scaled_positions[x].length; y++){
            if(positions[x][y]){
                ctx.fillRect(x, y, 1, 1);
            }
        }
    }
}

function scale(canvas){
    if(highest_x > canvas.width || highest_y > canvas.height){
        var scaled_positions = [];
        var x_percentage = highest_x / canvas.width;
        var y_percentage = highest_y / canvas.height;
        var raw_delta_x;
        var raw_delta_y;
        var delta;

        if(x_percentage > y_percentage){
            delta = Math.floor(x_percentage);
            raw_delta_x = x_percentage
            raw_delta_y = x_percentage
        } else {
            delta = Math.floor(y_percentage);
            raw_delta_x = y_percentage
            raw_delta_y = y_percentage
        }

        for(x = 0; x < map; x = x + raw_delta_x){
            var round_x = Math.floor(x);
            var y_positions = [];
            for (var y = 0; y < map[round_x].length; y = y + delta){
                var round_y = Math.floor(y);
                var set_1 = map[round_x - 1][round_y] && (map[round_x][round_y -1] || map[round_x + 1][round_y] || map[round_x][round_y + 1]);
                var set_2 = map[round_x + 1][round_y] && (map[round_x][round_y -1] || map[round_x][round_y + 1]);
                var set_3 = map[round_x][round_y - 1] && map[round_x][round_y + 1];
                if(set_1 || set_2 || set_3){
                    y_positions.push(true);
                } else if((map[round_x - 1][round_y] || map[round_x][round_y -1] || map[round_x + 1][round_y] || map[round_x][round_y + 1]) && y % 1 > 0.49){
                    y_positions.push(true);
                } else {
                    y_positions.push(false);
                }
                y_positions.push(map[round_x][round_y]);
            }
            scaled_positions[x].push(y_positions);
        }

        return scaled_positions;
    } else {
        return map;
    }
}

function createMap(){
    for (var x = 0; x < highest_x; x++){
        var y_positions = [];
        for(var y = 0; y < highest_y; y++){
            y_positions.push(false);
        }
        map.push(y_positions);
    }

    positions.forEach(function(pos){
        map[pos.x][pos.y] = true;
    });
}