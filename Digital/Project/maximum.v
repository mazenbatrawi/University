module maximum(a,b,max);
input [3:0] a,b;
output reg [3:0] max;
always @(a,b)
	begin
		if (a>b) 
			begin
				 max = a;
			end	
		else
			begin
			     max = b;
			end
	end
endmodule