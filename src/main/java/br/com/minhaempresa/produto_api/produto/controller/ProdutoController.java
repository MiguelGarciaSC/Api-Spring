package br.com.minhaempresa.produto_api.produto.controller;

import br.com.minhaempresa.produto_api.produto.entity.Produto;
import br.com.minhaempresa.produto_api.produto.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@Validated @RequestBody Produto produto) {
        Produto novoProduto = produtoService.salvarProduto(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarProsutos() {
        List<Produto> produtos = produtoService.listarProdutos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.buscarProdutoPorId(id);
        return produto.map(ResponseEntity::ok).
                orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(
        @PathVariable Long id,
        @Validated @RequestBody Produto produtoAtualizado) {
        Optional<Produto> produtoExistente = produtoService.buscarProdutoPorId(id);

        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            produto.setNome(produtoAtualizado.getNome());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setEstoque(produtoAtualizado.getEstoque());

            Produto produtoSalvo = produtoService.salvarProduto(produto);
            return ResponseEntity.ok(produtoSalvo);
        }   else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.buscarProdutoPorId(id);

        if (produto.isPresent()) {
            produtoService.deletarProduto(id);
            return ResponseEntity.noContent().build();
        }   else{
            return ResponseEntity.notFound().build();
        }
    }
}

