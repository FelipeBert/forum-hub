resource "aws_db_instance" "forum_hub_db" {
  allocated_storage      = 10
  db_name                = "mydb"
  db_subnet_group_name   = aws_db_subnet_group.forum_hub_db.id
  vpc_security_group_ids = [aws_security_group.rds-sg.id]
  engine                 = "mysql"
  engine_version         = "8.0"
  instance_class         = "db.t3.micro"
  username               = "foo"
  password               = "foobarbaz"
  parameter_group_name   = "default.mysql8.0"
  skip_final_snapshot    = true
}

resource "aws_db_subnet_group" "forum_hub_db" {
  name       = "forum-hub"
  subnet_ids = var.agent_subnet_id

  tags = {
    Name = "Forum Hub DB"
  }
}
