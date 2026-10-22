// A NAT gateway needs a public Elastic IP and a public subnet so private
// subnet instances can initiate outbound connections through it.
resource "aws_nat_gateway" "nat_gateway" {
  allocation_id = aws_eip.nat_gateway.id
  subnet_id     = aws_subnet.public[0].id

  tags = {
    Name = "forum-hub-nat-gateway"
  }

  depends_on = [
    aws_route.public_internet,
    aws_route_table_association.public,
  ]
}

resource "aws_eip" "nat_gateway" {
  domain = "vpc"

  tags = {
    Name = "forum-hub-nat-gateway"
  }
}
