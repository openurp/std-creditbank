package org.openurp.std.creditbank.web.action

import org.beangle.cdi.bind.BindModule

class DefaultModule extends BindModule {

  override protected def binding(): Unit = {
    bind(classOf[CourseAction])
    bind(classOf[CertificateAction])
    bind(classOf[ExchangeAction])
  }
}
