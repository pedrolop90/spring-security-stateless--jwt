resource "aws_security_group" "sg_ssh_testing_2" {
  name        = "sg_ssh_testing_1"
  description = "Primer test security ssh"

  ingress {
    description = "ssh"
    from_port   = 22
    to_port     = 22
    protocol    = "TCP"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    ENV = "DEV",
    GROUP = "DEFAULT",
    VERSION = "1.0"
    PROJECT = var.project_name
  }
}

resource "aws_security_group" "sg_http_testing_2" {
  name        = "sg_http_testing_1"
  description = "Primer test security http"

  ingress {
    description = "http"
    from_port   = 8010
    to_port     = 8010
    protocol    = "TCP"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    ENV = "DEV",
    GROUP = "DEFAULT",
    VERSION = "1.0"
    PROJECT = var.project_name
  }
}