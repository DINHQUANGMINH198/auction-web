import React from 'react'
import { Container, Row } from 'reactstrap'

const Footer = () => {
  return (
   <Container className="footer bottom bg-light bg-gradient rounded-top border-top border-5" fluid>
    <Container className="mt-1 text-center">
      <Row>        
        <div className="mx-auto pt-3">
          <p className="mb-0 font-weight-bold">&copy; 2023 MINHDQ</p>
          <hr className="my-0 bg-secondary"/>
          <p className="mb-0">email: minhdq198@gmail.com</p>
          <p>https://github.com/DINHQUANGMINH198</p>
        </div>
      </Row>      
    </Container>
    </Container>
  )
}

export default Footer
