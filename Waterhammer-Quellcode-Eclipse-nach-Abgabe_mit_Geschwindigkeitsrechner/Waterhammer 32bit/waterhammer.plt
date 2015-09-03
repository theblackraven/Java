joukowsky=13.69
set xlabel "Zeit in ms"  font "Helvetica,14" 
set ylabel "Druck in bar"  font "Helvetica,14"
set y2label "Ereignis"  font "Helvetica,14" offset 2,0 tc rgb "#0000ff"
set y2range [0:8]
set y2tics  font "Helvetica,14" tc rgb "#0000ff" nomirror
set ytics font "Helvetica,14" nomirror
set xtics font "Helvetica,14"
set datafile separator ";"
set y2tics ("R1" 1, "Druckstoß" 2, "Button SW1" 3, "Manuell" 4, "R5" 5, "R6" 6, "R7" 7 )
set grid y2tics lt 0 lw 1 lc 3
set key font "Helvetica,14"
plot "plot_current.csv" every ::3 using ($3/1000):($2/1000) with lines  title "Drucksensor 2" lw 1 axes x1y1  ,  "plot_current.csv"  every ::3 using ($3/1000):($1/1000) with lines lw 1 title "Drucksensor 1" axes x1y1, "plot_current.csv" every ::3 using ($3/1000):($4)  title "Ereignis" lw 1  axes x1y2 with lines , joukowsky title "Joukowsky-Stoß"





