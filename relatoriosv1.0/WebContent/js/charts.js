function drawVisualization(div,relatorio,config) {
        // Create and populate the data table.
        var data = google.visualization.arrayToDataTable(relatorio);
      
        // Create and draw the visualization.
        new google.visualization.ColumnChart(document.getElementById(div)).
            draw(data,
                 {title:config.group.name,
                  width:600, height:400,
                  hAxis: {title: config.service}}
            );
}
