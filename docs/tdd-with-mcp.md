# TDD with MCP

## Examples

### Bowling Game
- [youtube](https://github.com/msbaek/tdd-workshop/pull/2)
- [github PR](https://github.com/msbaek/tdd-workshop/pull/2)
## github PRs

- [Word Wrap TDD with MCP](https://github.com/msbaek/tdd-workshop/pull/3)
- [Shopping Basket TDD with MCP 1](https://claude.ai/share/fb917cce-3b60-492b-aa40-79c7cc62fa92)
- [Shopping Basket TDD with MCP 2](https://claude.ai/share/30d46a02-a5d9-4bbc-91c3-410edad5dfe2)

## add empty files

- Use Case, 기능 등을 의미하는 파일셋을 만들어 놓고 시작
    - ex. BowlingGame.java, BowlingGameTest.java, BowlingGame.md
- 관련 파일들을 열어 놓고, 작업 절차를 생성을 요청하는 것으로 시작

## add plan

```markdown
먼저 작업 절차를 작성해줘
``` 

## add SRS

```markdown
jetbrains intellij 툴을 사용하거나 코드, 마크다운을 작성하지 말고, 나와 논의만 하자.

Word Wrap
Write a function that takes two arguments, a string, and a column number. The function returns the string, but with line
breaks inserted at just the right places to make sure that no line is longer than the column number. You try to break
lines at word boundaries.
Like a word processor, break the line by replacing the last space in a line with a newline.

이런 기능을 TDD로 구현하려는 요구사항을 명확하게 했으면 해.
명확한 SRS 작성을 위해 필요한 질문을 내게 해줘
```

## add Example

### 예제 논의하기

```markdown
예제2은 불필요.
4,5번은 하나로 합쳐도 될 것 같아.
로직을 구현하는데 영향이 없어 보여.
어떻게 생각해 ?
```

## add High Level Test

- 목표 설계(target design)

## Walking Skeleton

## add test case list

## R-G-B

### add failing test

```markdown
실패하지 않는 테스트라서 굳이 추가할 필요가 있을까 ?
```

### degenerate test

### one step test

### As the tests get more specific, the production code gets more generic

### mark done in test case list

### fake it

```markdown
내가 테스트 에서 중복을 제거하기 위해 리팩터링을 했어.
그리고 모델 코드는 지금까지는 그냥 ""만 반환(fake it)해도 성공해서 수정했어.

내 변경을 확인하고 다음 테스트르 추가해줘 
```

### 검증하기

```markdown
void handle_multiple_spaces() {
assertWraps("hello world", 12, "hello world");
}

이 테스트에 expected가 잘 못 되었어
```

### 개입하기

```markdown
내가 테스트와 구현을 변경했어. 확인하고 의견을 줘.
이 부분에 대해서 자세히 논의했으면 해 
```

## Four Layer Separation Of Concerns