module BCDAS(a,b,cin,result,cout);
input [3:0] a,b;
input cin;
output cout; 
output [3:0] result;
wire w1,w2,car1,car2;
wire [3:0] err,temp_sum,temp_sum2,ten_comp;
 
and (ten_comp[3],cin,cin);
and (ten_comp[1],cin,cin);
xor (ten_comp[2],cin,cin);
xor (ten_comp[0],cin,cin);

fbAS (ten_comp,b,cin,temp_sum,car1);
fbAS (a,temp_sum,1'b0,temp_sum2,car2);

and(w1,temp_sum2[3],temp_sum2[2]);
and (w2,temp_sum2[3],temp_sum2[1]);

or(err[2],car2,w1,w2);
or(err[1],car2,w1,w2);
xor(err[3],car2,car2);
xor(err[0],car2,car2);

fbAS finish(temp_sum2,err,1'b0,result,cout);
endmodule