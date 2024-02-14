module minimum(a,b,min);
input [3:0] a,b;
output reg [3:0] min;
always @(a,b)
	begin
		if (a<b) 
			begin
				 min = a;
			end	
		else
			begin
			     min = b;
			end
	end
endmodule