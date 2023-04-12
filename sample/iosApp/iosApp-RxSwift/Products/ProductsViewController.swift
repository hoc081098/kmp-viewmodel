//
//  ViewController.swift
//  iosApp-RxSwift
//
//  Created by Petrus Nguyen Thai Hoc on 4/11/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import UIKit
import shared
import RxSwift
import NSObject_Rx
import Kingfisher
import RxRelay
import RxDataSources
import MBProgressHUD

class ProductTableViewCell: UITableViewCell {

  @IBOutlet weak var descriptionLabel: UILabel!
  @IBOutlet weak var titleLabel: UILabel!
  @IBOutlet weak var productImageView: UIImageView!

  func configure(with item: ProductItemUi) {
    let url = item.images.first.flatMap(URL.init(string:))

    KF.url(url)
      .cacheOriginalImage()
      .onFailure { e in Napier.e(error: e, "err: url=\(String(describing: url)), e=\(e)") }
      .fade(duration: 1)
      .forceTransition()
      .set(to: productImageView)

    titleLabel.text = item.title
    descriptionLabel.text = item.description_
  }
}

class ProductsViewController: UIViewController {

  private let vm = IosProductsViewModel()

  @IBOutlet weak var tableView: UITableView!

  override func viewDidLoad() {
    super.viewDidLoad()

    self.tableView.estimatedRowHeight = 72 + 16 * 2
    self.tableView.rowHeight = UITableView.automaticDimension

    bindVm()

    // Auto retry on error
    self.vm.state$
      .map(\.error)
      .distinctUntilChanged()
      .filter { $0 != nil }
      .map { _ in ProductsActionLoad() }
      .startWith(ProductsActionLoad())
      .drive(onNext: vm.dispatch)
      .disposed(by: self.rx.disposeBag)
  }

  override func viewDidAppear(_ animated: Bool) {
    super.viewDidAppear(animated)
    self.vm.onActive()
  }

  override func viewWillDisappear(_ animated: Bool) {
    super.viewWillDisappear(animated)
    self.vm.onInactive()
  }

  deinit {
    Napier.d("\(self)::deinit")
  }
}

private extension ProductsViewController {
  func bindVm() {
    let dataSource = RxTableViewSectionedAnimatedDataSource<AnimatableSectionModel<String, ProductItemUi>> { dataSource, tableView, indexPath, item in
      let cell = tableView.dequeueReusableCell(withIdentifier: "ProductTableViewCell", for: indexPath) as! ProductTableViewCell
      cell.configure(with: item)
      return cell
    }

    self.vm.state$
      .map { state in [.init(model: "", items: state.products)] }
      .drive(self.tableView.rx.items(dataSource: dataSource))
      .disposed(by: self.rx.disposeBag)

    self.vm.state$
      .map(\.isLoading)
      .distinctUntilChanged()
      .drive(with: self, onNext: { vc, v in
      if v {
        MBProgressHUD.showAdded(to: vc.view, animated: true)
      } else {
        MBProgressHUD.hide(for: vc.view, animated: true)
      }
    })
      .disposed(by: self.rx.disposeBag)
  }
}

extension ProductItemUi: IdentifiableType {
  public var identity: Int32 { self.id }
}
